package com.test.otus_film_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.otus_film_app.di.qualifiers.KinopoiskQualifier
import com.test.otus_film_app.repository.FilmRepository
import javax.inject.Inject

class FilmViewModelFactory @Inject constructor(val filmRepository: FilmRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FilmViewModel(filmRepository) as T
    }
}