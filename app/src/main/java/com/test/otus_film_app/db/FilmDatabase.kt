package com.test.otus_film_app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.DataConverter

@Database(entities = [Film::class], version = 3, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class FilmDatabase: RoomDatabase() {
    abstract fun filmDao(): FilmDao
}