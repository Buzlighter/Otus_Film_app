package com.test.otus_film_app.util

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class KinopoiskInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("X-API-KEY", Constants.KINOPOISK_API_KEY)
                .build()
        )
    }
}