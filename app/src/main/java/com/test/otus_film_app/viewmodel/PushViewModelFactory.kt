package com.test.otus_film_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.otus_film_app.api.NotificationAPI
import com.test.otus_film_app.repository.FilmRepository
import javax.inject.Inject

class PushViewModelFactory @Inject constructor(val notificationAPI: NotificationAPI): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PushServiceViewModel(notificationAPI) as T
    }
}