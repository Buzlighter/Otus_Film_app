package com.test.otus_film_app.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.otus_film_app.model.Country
import com.test.otus_film_app.model.Genre

class DataConverter {

    @TypeConverter
    fun fromCountryList(value: List<Country>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Country>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCountryList(value: String): List<Country> {
        val gson = Gson()
        val type = object : TypeToken<List<Country>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromGenreList(value: List<Genre>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGenreList(value: String): List<Genre> {
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(value, type)
    }
}