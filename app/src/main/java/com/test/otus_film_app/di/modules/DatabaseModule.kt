package com.test.otus_film_app.di.modules

import android.content.Context
import androidx.room.Room
import com.test.otus_film_app.db.FilmDao
import com.test.otus_film_app.db.FilmDatabase
import com.test.otus_film_app.util.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(context: Context): FilmDatabase {
           return Room.databaseBuilder(
                context,
                FilmDatabase::class.java,
                Constants.DB_NAME
            ).build()
    }

    @Singleton
    @Provides
    fun provideFilmDao(filmDatabase: FilmDatabase): FilmDao {
        return filmDatabase.filmDao()
    }
}