package com.test.otus_film_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.otus_film_app.App
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.repository.FilmRepository
import com.test.otus_film_app.util.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FilmViewModel(val filmRepository: FilmRepository) : ViewModel() {
    private val localFilmData = MutableLiveData<Resource<KinopoiskResponse>>()
    val filmData: LiveData<Resource<KinopoiskResponse>> = localFilmData

    private val localCacheData = MutableLiveData<List<Film>>()
    val cacheData: LiveData<List<Film>> = localCacheData

    var requestDateYear = "2022"
    var requestedMonth = "JUNE"

    val compositeDisposable = CompositeDisposable()

    init {
        getFilms()
    }

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


    fun getFilms() {
        compositeDisposable.add(
            filmRepository.filmApi
                .getFilmsPremier(requestDateYear, requestedMonth)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    localFilmData.postValue(Resource.Loading())
                }
                .doFinally {
                    localCacheData.postValue(App.appComponent.getFilmDao().getAll())
                }
                .subscribe({ successData ->
                    insertDataIntoDb(successData.filmList)
                    localFilmData.postValue(Resource.Success(successData))
                }, {
                    localFilmData.postValue(Resource.Error(null))
                })
        )
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
        val dbContainer = App.appComponent.getFilmDao().checkIsDbContain()
        if (dbContainer == 0) {
            App.appComponent.getFilmDao().insertFilmList(filmList)
        } else {
            filmList.forEach { film ->
                film.let {
                    App.appComponent.getFilmDao().updateFilmFields(
                        it.id, it.nameRu, it.nameEn, it.year, it.posterUrl,
                        it.posterUrlPreview, it.countryList, it.genreList,
                        it.duration, it.premiereRu
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}