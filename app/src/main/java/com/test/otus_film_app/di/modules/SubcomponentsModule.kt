package com.test.otus_film_app.di.modules

import com.test.otus_film_app.di.components.FilmListComponent
import com.test.otus_film_app.di.components.MainActivityComponent
import com.test.otus_film_app.di.components.NotificationComponent
import dagger.Module

@Module(subcomponents = [FilmListComponent::class, NotificationComponent::class, MainActivityComponent::class])
interface SubcomponentsModule