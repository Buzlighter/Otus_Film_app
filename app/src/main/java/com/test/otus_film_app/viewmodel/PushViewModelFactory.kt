package com.test.otus_film_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.otus_film_app.api.NotificationAPI
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PushViewModelFactory @Inject constructor(val notificationAPI: NotificationAPI,
                                               val dispatcher: CoroutineDispatcher): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PushServiceViewModel(notificationAPI, dispatcher) as T
    }
}