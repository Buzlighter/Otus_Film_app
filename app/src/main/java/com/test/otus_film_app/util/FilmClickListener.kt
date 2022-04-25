package com.test.otus_film_app.util

import com.test.otus_film_app.model.Film

interface FilmClickListener {
    fun onFilmClick(film: Film, position: Int = 0)
    fun onFilmLongClick(film: Film)
}