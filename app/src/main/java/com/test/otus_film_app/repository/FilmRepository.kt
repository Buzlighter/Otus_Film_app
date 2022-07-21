package com.test.otus_film_app.repository

import com.test.otus_film_app.App.Companion.appComponent
import com.test.otus_film_app.api.FilmApi
import com.test.otus_film_app.api.NotificationAPI
import com.test.otus_film_app.db.FilmDao
import com.test.otus_film_app.model.Film
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class FilmRepository @Inject constructor(val filmApi: FilmApi, val filmDao: FilmDao) {

    var requestDateYear = "2022"
    var requestedMonth = "JUNE"

    val compositeDisposable = CompositeDisposable()

//    fun getFilms() = viewModelScope.launch(Dispatchers.IO) {
//        FilmService.filmApi.let {
//            try {
//                localFilmData.postValue(Resource.Loading())
//                val responseData = it.getFilmsPremier(requestDateYear, requestedMonth)
//                localFilmData.postValue(handleResponse(responseData))
//            } catch (e: Exception) {
//                localFilmData.postValue(Resource.Error(null))
//            } finally {
//                localCacheData.postValue(App.filmDB.filmDao().getAll())
//            }
//        }
//    }


    fun getFilms(): FilmApi{
        return filmApi
    }

//    private fun handleResponse(responseData: Response<KinopoiskResponse>): Resource<KinopoiskResponse> {
//        if (responseData.isSuccessful) {
//            responseData.body()?.let { data ->
//                insertDataIntoDb(data.filmList)
//                return Resource.Success(data)
//            }
//        }
//        return Resource.Error(null)
//    }

    /*
    Проверям пустая ли база, и если пустая, то заполняем ее через Insert, если нет, то
    обновляем все поля кроме isFavorite, чтобы не переписывались значением по умлочанию (false)
    */
    private fun insertDataIntoDb(filmList: List<Film>) {
        val dbContainer = appComponent.getFilmDao().checkIsDbContain()
        if (dbContainer == 0) {
            appComponent.getFilmDao().insertFilmList(filmList)
        } else {
            filmList.forEach { film ->
                film.let {
                    appComponent.getFilmDao().updateFilmFields(
                        it.id, it.nameRu, it.nameEn, it.year, it.posterUrl,
                        it.posterUrlPreview, it.countryList, it.genreList,
                        it.duration, it.premiereRu
                    )
                }
            }
        }
    }
}