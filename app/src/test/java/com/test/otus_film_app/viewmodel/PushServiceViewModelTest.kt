package com.test.otus_film_app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.otus_film_app.api.NotificationAPI
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.NotificationData
import com.test.otus_film_app.model.PushNotification
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertNotNull
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
class PushServiceViewModelTest {
    val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var notificationAPI: NotificationAPI

    lateinit var pushViewModel: PushServiceViewModel

    @Before
    fun setUpPushViewModel() {
        pushViewModel = PushServiceViewModel(notificationAPI, testDispatcher)
    }

    @Test
    fun sendNotification() = runTest {
        val film = Film(1,"S","S", 2000,
            "S", "S", listOf(), listOf(), 1, "s", true, "s")
        val notificationn = PushNotification(NotificationData("s","s",film),"s")


        val expected = Response.success( notificationn.toString().toResponseBody())
        Mockito.`when`(notificationAPI.postNotification(notificationn)).thenReturn(expected)

        assertNotNull(expected)
    }

    @Test
    fun getNotificationAPI() {
    }
}