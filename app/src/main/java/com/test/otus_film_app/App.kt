package com.test.otus_film_app

import android.app.Application
import android.os.Build
import com.google.firebase.FirebaseApp
import com.test.otus_film_app.di.components.AppComponent
import com.test.otus_film_app.di.components.DaggerAppComponent


class App: Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            FirebaseApp.initializeApp(this)
        }

        appComponent = DaggerAppComponent.builder().appContext(this).build()

        appComponent.inject(this)
    }

}