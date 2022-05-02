package com.test.otus_film_app

import android.app.Application
import com.test.otus_film_app.api.HttpClient
import com.test.otus_film_app.db.FilmDatabase

class App: Application() {

    companion object {
        lateinit var filmDB: FilmDatabase
        lateinit var arrayOfPosition: MutableSet<Int>
    }

    override fun onCreate() {
        super.onCreate()
        HttpClient.configureClient()
        filmDB = FilmDatabase.getDatabase(this)

        arrayOfPosition = mutableSetOf()
    }
}