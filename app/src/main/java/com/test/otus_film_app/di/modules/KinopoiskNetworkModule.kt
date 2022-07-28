package com.test.otus_film_app.di.modules

import com.test.otus_film_app.di.qualifiers.KinopoiskQualifier
import com.test.otus_film_app.util.Constants
import com.test.otus_film_app.util.KinopoiskInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object KinopoiskNetworkModule {

    @Singleton
    @Provides
    @KinopoiskQualifier
    fun provideClient(kinopoiskInterceptor: KinopoiskInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(kinopoiskInterceptor)
            .addNetworkInterceptor(logging).build()
    }

    @Singleton
    @Provides
    @KinopoiskQualifier
    fun provideRetrofit(@KinopoiskQualifier okHttpClient: OkHttpClient): Retrofit {
        return  Retrofit.Builder()
            .baseUrl(Constants.KINOPOISK_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}