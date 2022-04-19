package com.test.otus_film_app.api

import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.util.Access.Companion.PREMIER_ENDPOINT
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmApi {

    @GET(PREMIER_ENDPOINT)
    suspend fun getFilmsPremier(
        @Query("year") year: String,
        @Query("month") month: String
    ): KinopoiskResponse
}