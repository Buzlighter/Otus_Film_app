package com.test.otus_film_app.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Film(
    @SerializedName("kinopoiskId")
    val id: Int?,
    @SerializedName("nameRu")
    val nameRu: String?,
    @SerializedName("nameEn")
    val nameEn: String?,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("posterUrl")
    val posterUrl: String?,
    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String?,
    @SerializedName("countries")
    val countryList: List<Country?>,
    @SerializedName("genres")
    val genreList: List<Genre?>,
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("premiereRu")
    val premiereRu: String?
): Serializable