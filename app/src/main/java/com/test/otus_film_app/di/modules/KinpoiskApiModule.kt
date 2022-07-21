package com.test.otus_film_app.di.modules

import com.test.otus_film_app.api.FilmApi
import com.test.otus_film_app.di.qualifiers.KinopoiskQualifier
import com.test.otus_film_app.di.scopes.FragmentScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class KinpoiskApiModule {

    @FragmentScope
    @Provides
    fun provideKinopoiskApiService(@KinopoiskQualifier retrofit: Retrofit): FilmApi {
        return retrofit.create(FilmApi::class.java)
    }
}