package com.test.otus_film_app.di.components

import android.content.Context
import com.test.otus_film_app.di.modules.FirebaseRemoteModule
import com.test.otus_film_app.di.scopes.ActivityScope
import com.test.otus_film_app.view.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface FirebaseRemoteComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        fun firebaseModule(firebaseRemoteModule: FirebaseRemoteModule): Builder

        fun activityContext(@BindsInstance context: Context): Builder

        fun build(): FirebaseRemoteComponent
    }
}