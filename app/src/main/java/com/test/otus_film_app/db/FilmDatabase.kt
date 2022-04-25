package com.test.otus_film_app.db

import android.content.Context
import androidx.room.*
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.util.Access.Companion.DB_NAME
import com.test.otus_film_app.util.DataConverter

@Database(entities = [Film::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class FilmDatabase: RoomDatabase() {
    abstract fun filmDao(): FilmDao

    companion object {
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getDatabase(context: Context): FilmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    FilmDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}