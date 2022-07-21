package com.test.otus_film_app.di.components

import com.test.otus_film_app.di.modules.KinpoiskApiModule
import com.test.otus_film_app.di.scopes.FragmentScope
import com.test.otus_film_app.view.film_list_screen.FilmListFragment
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [KinpoiskApiModule::class])
interface FilmListComponent {

    fun inject(filmListFragment: FilmListFragment)

    @Subcomponent.Builder
    interface Builder {
        fun kinopoiskModule(kinopoiskApiModule: KinpoiskApiModule): Builder
        fun build(): FilmListComponent
    }
}