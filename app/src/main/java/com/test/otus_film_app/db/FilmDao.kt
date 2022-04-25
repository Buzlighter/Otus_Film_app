package com.test.otus_film_app.db

import androidx.room.*
import com.test.otus_film_app.model.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM film")
    fun getAll(): List<Film>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteFilm(film: Film)

    @Delete
    fun delete(film: Film)
}