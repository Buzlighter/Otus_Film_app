package com.test.otus_film_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.test.otus_film_app.api.NotificationService
import com.test.otus_film_app.model.PushNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PushServiceViewModel: ViewModel() {

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = NotificationService.api.postNotification(notification)
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