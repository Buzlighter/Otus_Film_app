package com.test.otus_film_app.di.modules

import com.test.otus_film_app.api.NotificationAPI
import com.test.otus_film_app.di.qualifiers.NotificationQualifier
import com.test.otus_film_app.di.scopes.FragmentScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create

@Module
class NotificationApiModule {

    @FragmentScope
    @Provides
    fun providesNotificationApiService(@NotificationQualifier retrofit: Retrofit): NotificationAPI {
        return retrofit.create(NotificationAPI::class.java)
    }
}