package com.test.otus_film_app.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Country(
    @SerializedName("country")
    val country: String?
): Serializable
