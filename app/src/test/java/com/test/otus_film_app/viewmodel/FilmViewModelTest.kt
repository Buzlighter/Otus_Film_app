package com.test.otus_film_app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.otus_film_app.api.FilmApi
import com.test.otus_film_app.db.FilmDao
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.KinopoiskResponse
import com.test.otus_film_app.repository.FilmRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FilmViewModelTest {
    val testDispatcher = UnconfinedTestDispatcher()


    @get:Rule
    val mockitoRule:MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var filmApi: FilmApi

    @Mock
    lateinit var filmDao: FilmDao

    lateinit var filmViewModel: FilmViewModel

    lateinit var filmRepository: FilmRepository


    @Before
    fun setUpFilmViewModel() {
        filmRepository = FilmRepository(filmApi, filmDao)
        filmViewModel = FilmViewModel(filmRepository, testDispatcher)
    }

    @Test
    fun `get film list from db`() = runTest {
        val expected = listOf<Film>()
        Mockito.`when`(filmDao.getAll()).thenReturn(expected)

        val resp  = filmRepository.filmDao.getAll()
        assertEquals(resp, expected)
    }

    @Test
    fun `get film list where result is success`() = runTest {
        val expected = Response.success(KinopoiskResponse(listOf()))
        Mockito.`when`(filmApi.getFilmsPremier("2022", "JUNE")).thenReturn(expected)

        val resp = filmRepository.getFilms("2022", "JUNE").data
        assertEquals(resp, expected.body())
    }

    @Test
    fun `get film list where result is error`() = runTest {
        val expected: Response<KinopoiskResponse>  = Response.error(401, "experiment".toResponseBody())
        Mockito.`when`(filmApi.getFilmsPremier("20221", "JUNE")).thenReturn(expected)

        val resp  = filmRepository.filmApi.getFilmsPremier("20221", "JUNE")
        assertEquals(resp, expected)
    }

    @Test(expected = Exception::class)
    fun `getFilms that throw exception when incorrect api input`()= runTest {
        Mockito.doThrow(Exception()).`when`(filmApi.getFilmsPremier(Mockito.anyString(), Mockito.anyString()))
    }

    @Test
    fun `check if is empty`() = runTest {
        val expected = 0
        Mockito.`when`(filmDao.checkIsDbContain()).thenReturn(expected)

        val resp = filmRepository.filmDao.checkIsDbContain()
        assertEquals(expected, resp)
    }
}