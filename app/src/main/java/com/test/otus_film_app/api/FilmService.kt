package com.test.otus_film_app.api

import com.test.otus_film_app.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FilmService {
    companion object {
        lateinit var filmApi: FilmApi

        fun configureClient() {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header("X-API-KEY", Constants.KINOPOISK_API_KEY)

                    val request = requestBuilder.build()
                    chain.proceed(request)
                }.addNetworkInterceptor(logging).build()

            configureRetrofit(httpClient)
        }

        private fun configureRetrofit(okHttpClient: OkHttpClient) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.KINOPOISK_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            filmApi = retrofit.create(FilmApi::class.java)
        }

    }
}