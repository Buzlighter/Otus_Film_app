package com.test.otus_film_app.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Genre(
    @SerializedName("genre")
    val genre: String?
): Serializable
