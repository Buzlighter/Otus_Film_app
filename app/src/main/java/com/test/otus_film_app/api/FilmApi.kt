package com.test.otus_film_app.api

import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.util.Constants.Companion.PREMIER_ENDPOINT
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmApi {

    @GET(PREMIER_ENDPOINT)
    fun getFilmsPremier(
        @Query("year") year: String,
        @Query("month") month: String
    ): Single<KinopoiskResponse>
}