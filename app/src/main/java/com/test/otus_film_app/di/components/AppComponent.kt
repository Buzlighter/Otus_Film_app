package com.test.otus_film_app.di.components

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.test.otus_film_app.App
import com.test.otus_film_app.db.FilmDao
import com.test.otus_film_app.di.modules.DatabaseModule
import com.test.otus_film_app.di.modules.KinopoiskNetworkModule
import com.test.otus_film_app.di.modules.NotificationNetworkModule
import com.test.otus_film_app.di.modules.SubcomponentsModule
import com.test.otus_film_app.util.CustomRemoteConfig
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [KinopoiskNetworkModule::class, NotificationNetworkModule::class, DatabaseModule::class, SubcomponentsModule::class])
interface AppComponent {

    fun filmListFragmentComponentBuilder(): FilmListComponent.Builder

    fun notificationFragmentComponentBuilder(): NotificationComponent.Builder

    fun mainActivityComponentBuilder(): MainActivityComponent.Builder

    fun inject(app: App)

    fun getFilmDao(): FilmDao

    fun getAppContext(): Context

    @Component.Builder
    interface Builder {

        fun appContext(@BindsInstance context: Context): Builder

        fun build(): AppComponent
    }

}