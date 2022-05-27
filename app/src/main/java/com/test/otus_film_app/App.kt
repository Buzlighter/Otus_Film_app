package com.test.otus_film_app

import android.app.Application
import com.test.otus_film_app.api.FilmService
import com.test.otus_film_app.db.FilmDatabase

class App: Application() {

    companion object {
        lateinit var filmDB: FilmDatabase
    }

    override fun onCreate() {
        super.onCreate()
        FilmService.configureClient()
        filmDB = FilmDatabase.getDatabase(this)
    }
}