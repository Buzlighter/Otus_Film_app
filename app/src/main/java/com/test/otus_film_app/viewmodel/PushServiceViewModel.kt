package com.test.otus_film_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.test.otus_film_app.App
import com.test.otus_film_app.api.NotificationAPI
import com.test.otus_film_app.api.NotificationService
import com.test.otus_film_app.model.PushNotification
import com.test.otus_film_app.repository.FilmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PushServiceViewModel(val notificationAPI: NotificationAPI): ViewModel() {

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = notificationAPI.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("MY_FCM", "Response: Success")
            } else {
                Log.e("MY_FCM", response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e("MY_FCM", e.toString())
        }
    }
}