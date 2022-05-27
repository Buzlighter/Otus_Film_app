package com.test.otus_film_app.db

import androidx.room.*
import com.test.otus_film_app.model.Country
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.Genre

@Dao
interface FilmDao {
    @Query("SELECT * FROM film")
    fun getAll(): List<Film>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteFilm(film: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmList(filmList: List<Film>)

    @Delete
    fun delete(film: Film)

    @Query("SELECT count(*) from film")
    fun checkIsDbContain(): Int

    @Query("UPDATE film SET nameRu = :nameRu, nameEn = :nameEn, year = :year, posterUrl = :posterUrl, " +
            "posterUrlPreview = :posterUrlPreview, countryList = :countryList, genreList = :genreList, " +
            "duration = :duration, premiereRu = :premiereRu WHERE id = :id")
    fun updateFilmFields(id: Int?, nameRu: String?, nameEn: String?,
                         year: Int?, posterUrl: String?, posterUrlPreview: String?,
                         countryList: List<Country?>, genreList: List<Genre?>,
                         duration: Int?, premiereRu: String?)
}