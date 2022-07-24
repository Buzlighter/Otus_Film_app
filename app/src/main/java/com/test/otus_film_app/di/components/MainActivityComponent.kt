package com.test.otus_film_app.di.components

import com.test.otus_film_app.di.scopes.ActivityScope
import com.test.otus_film_app.view.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainActivityComponent {

    fun inject(mainActivity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        fun create(): MainActivityComponent
    }
}