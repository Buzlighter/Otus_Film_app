package com.test.otus_film_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.otus_film_app.App
import com.test.otus_film_app.api.FilmService
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class FilmViewModel : ViewModel() {
    private val localFilmData = MutableLiveData<Resource<KinopoiskResponse>>()
    val filmData: LiveData<Resource<KinopoiskResponse>> = localFilmData

    val localCacheData = MutableLiveData<List<Film>>()
    val cacheData: LiveData<List<Film>> = localCacheData

    init {
        getFilms()
    }

    fun getFilms() = viewModelScope.launch(Dispatchers.IO) {
        FilmService.filmApi.let {
            try {
                localFilmData.postValue(Resource.Loading())
                val responseData = it.getFilmsPremier("2022", "JUNE")
                localFilmData.postValue(handleResponse(responseData))
            } catch (e: Exception) {
                localCacheData.postValue(App.filmDB.filmDao().getAll())
                localFilmData.postValue(Resource.Error(null))
            }
        }
    }

    private fun handleResponse(responseData: Response<KinopoiskResponse>): Resource<KinopoiskResponse> {
        if (responseData.isSuccessful) {
            responseData.body()?.let { data ->
                insertDataIntoDb(data.filmList)
                localCacheData.postValue(App.filmDB.filmDao().getAll())
                return Resource.Success(data)
            }
        }
        localCacheData.postValue(App.filmDB.filmDao().getAll())
        return Resource.Error(null)
    }

    /*
    Проверям пустая ли база, и если пустая, то заполняем ее через Insert, если нет, то
    обновляем все поля кроме isFavorite, чтобы не переписывались значением по умлочанию (false)
    */
    private fun insertDataIntoDb(filmList: List<Film>) {
        val dbContainer = App.filmDB.filmDao().checkIsDbContain()
        if (dbContainer == 0) {
            App.filmDB.filmDao().insertFilmList(filmList)
        } else {
            filmList.forEach { film ->
                film.let {
                    App.filmDB.filmDao().updateFilmFields(
                        it.id, it.nameRu, it.nameEn, it.year, it.posterUrl,
                        it.posterUrlPreview, it.countryList, it.genreList,
                        it.duration, it.premiereRu
                    )
                }
            }
        }
    }

}