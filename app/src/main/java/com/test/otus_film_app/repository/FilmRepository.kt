package com.test.otus_film_app.repository

import com.test.otus_film_app.api.FilmApi
import com.test.otus_film_app.db.FilmDao
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.util.Resource
import javax.inject.Inject

class FilmRepository @Inject constructor(val filmApi: FilmApi, val filmDao: FilmDao) {

    suspend fun getFilms(year: String, month: String): Resource<KinopoiskResponse> {
        val response = filmApi.getFilmsPremier(year, month)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                insertDataIntoDb(responseBody.filmList)
                Resource.Success(responseBody)
            } else {
                Resource.Error(response.body())
            }
        } else {
            Resource.Error(response.body())
        }
    }


    /*
    Проверям пустая ли база, и если пустая, то заполняем ее через Insert, если нет, то
    обновляем все поля кроме isFavorite, чтобы не переписывались значением по умлочанию (false)
    */
    fun insertDataIntoDb(filmList: List<Film>) {
        val dbContainer = filmDao.checkIsDbContain()
        if (dbContainer == 0) {
            filmDao.insertFilmList(filmList)
        } else {
            filmList.forEach { film ->
                film.let {
                    filmDao.updateFilmFields(
                        it.id, it.nameRu, it.nameEn, it.year, it.posterUrl,
                        it.posterUrlPreview, it.countryList, it.genreList,
                        it.duration, it.premiereRu
                    )
                }
            }
        }
    }
}