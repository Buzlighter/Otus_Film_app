package com.test.otus_film_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.otus_film_app.api.HttpClient
import com.test.otus_film_app.model.KinopoiskResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmViewModel: ViewModel() {

    val filmData = MutableLiveData<KinopoiskResponse>()

    fun getFilms() {
        viewModelScope.launch(Dispatchers.IO) {
            HttpClient.filmApi.let {
                filmData.postValue(
                    it.getFilmsPremier(
                        "2022",
                        "MAY"
                    )
                )
            }
        }

    }

}