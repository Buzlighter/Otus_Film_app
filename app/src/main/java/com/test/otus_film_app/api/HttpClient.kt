package com.test.otus_film_app.api

import com.test.otus_film_app.util.Access
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HttpClient {
    companion object {
        lateinit var filmApi: FilmApi

        fun configureClient() {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder().
            addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("X-API-KEY", Access.KINOPOISK_API_KEY)

                val request = requestBuilder.build()
                chain.proceed(request)
            }.addNetworkInterceptor(logging).build()

            configureRetrofit(httpClient)
        }

        private fun configureRetrofit(okHttpClient: OkHttpClient) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Access.KINOPOISK_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            filmApi = retrofit.create(FilmApi::class.java)
        }

    }
}