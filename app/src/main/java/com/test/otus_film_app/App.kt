package com.test.otus_film_app

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
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
        FirebaseApp.initializeApp(this)
    }

}