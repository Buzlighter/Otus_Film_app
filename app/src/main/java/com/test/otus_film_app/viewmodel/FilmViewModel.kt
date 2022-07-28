package com.test.otus_film_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.repository.FilmRepository
import com.test.otus_film_app.util.Resource
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class FilmViewModel(val filmRepository: FilmRepository, val dispatcherIO: CoroutineDispatcher) : ViewModel() {
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

//    fun getFilms() = viewModelScope.launch(dispatcherIO) {
//        filmRepository.filmApi.let {
//            try {
//                localFilmData.postValue(Resource.Loading())
//                val responseData = it.getFilmsPremier(requestDateYear, requestedMonth)
//                localFilmData.postValue(handleResponse(responseData))
//            } catch (e: Exception) {
//                e.printStackTrace()
//                localFilmData.postValue(Resource.Error(null))
//            } finally {
//                localCacheData.postValue(filmRepository.filmDao.getAll())
//            }
//        }
//    }

    fun getFilms() = viewModelScope.launch(dispatcherIO) {
        try {
            localFilmData.postValue(Resource.Loading())
            localFilmData.postValue(filmRepository.getFilms(requestDateYear, requestedMonth))
        } catch (e: Exception) {
            localFilmData.postValue(Resource.Error(null))
        } finally {
            localCacheData.postValue(filmRepository.filmDao.getAll())
        }
    }

// -----RXJava-----

//    fun getFilms() {
//        compositeDisposable.add(
//            filmRepository.filmApi
//                .getFilmsPremier(requestDateYear, requestedMonth)
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe {
//                    localFilmData.postValue(Resource.Loading())
//                }
//                .doFinally {
//                    localCacheData.postValue(App.appComponent.getFilmDao().getAll())
//                }
//                .doOnSuccess {
//                    filmRepository.insertDataIntoDb(it.filmList)
//                }
//                .subscribe({ successData ->
//                    Log.d("taglist", Thread.currentThread().toString())
//                    localFilmData.postValue(Resource.Success(successData))
//                }, {
//                    localFilmData.postValue(Resource.Error(null))
//                })
//        )
//    }

//    fun handleResponse(responseData: Response<KinopoiskResponse>): Resource<KinopoiskResponse> {
//        if (responseData.isSuccessful) {
//            responseData.body()?.let { data ->
//                filmRepository.insertDataIntoDb(data.filmList)
//                return Resource.Success(data)
//            }
//        }
//        return Resource.Error(null)
//    }

    override fun onCleared() {
        super.onCleared()
//        compositeDisposable.clear()

    }

}