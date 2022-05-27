package com.test.otus_film_app.util

sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(data: T?): Resource<T>(data)
    class Loading<T> : Resource<T>()
}