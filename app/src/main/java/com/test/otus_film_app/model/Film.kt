package com.test.otus_film_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "film")
data class Film(
    @PrimaryKey
    @SerializedName("kinopoiskId")
    val id: Int?,
    @ColumnInfo(name = "nameRu")
    @SerializedName("nameRu")
    val nameRu: String?,
    @ColumnInfo(name = "nameEn")
    @SerializedName("nameEn")
    val nameEn: String?,
    @ColumnInfo(name = "year")
    @SerializedName("year")
    val year: Int?,
    @ColumnInfo(name = "posterUrl")
    @SerializedName("posterUrl")
    val posterUrl: String?,
    @ColumnInfo(name = "posterUrlPreview")
    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String?,
    @ColumnInfo(name = "countryList")
    @SerializedName("countries")
    val countryList: List<Country?>,
    @ColumnInfo(name = "genreList")
    @SerializedName("genres")
    val genreList: List<Genre?>,
    @ColumnInfo(name = "duration")
    @SerializedName("duration")
    val duration: Int?,
    @ColumnInfo(name = "premiereRu")
    @SerializedName("premiereRu")
    val premiereRu: String?
): Serializable