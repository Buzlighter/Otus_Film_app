package com.test.otus_film_app

import android.app.Application
import com.test.otus_film_app.api.HttpClient

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        HttpClient.configureClient()
    }
}