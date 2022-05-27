package com.test.otus_film_app.model

import com.google.gson.annotations.SerializedName

data class KinopoiskResponse(
    @SerializedName("items")
    val filmList: List<Film>
)