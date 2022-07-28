package com.test.otus_film_app.di.modules

import com.test.otus_film_app.di.qualifiers.NotificationQualifier
import com.test.otus_film_app.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NotificationNetworkModule {

    @Singleton
    @Provides
    @NotificationQualifier
    fun provideClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addNetworkInterceptor(logging).build()
    }

    @Singleton
    @Provides
    @NotificationQualifier
    fun provideRetrofit(@NotificationQualifier okHttpClient: OkHttpClient): Retrofit {
        return  Retrofit.Builder()
            .baseUrl(Constants.NOTIFICATION_GCM_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}