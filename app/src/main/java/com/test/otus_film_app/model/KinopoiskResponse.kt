package com.test.otus_film_app.model

import com.google.gson.annotations.SerializedName

data class KinopoiskResponse(
    @SerializedName("total")
    val total: Int?,
    @SerializedName("items")
    val filmList: ArrayList<Film>
)