package com.test.otus_film_app.di.components

import com.test.otus_film_app.api.NotificationAPI
import com.test.otus_film_app.di.modules.KinpoiskApiModule
import com.test.otus_film_app.di.modules.NotificationApiModule
import com.test.otus_film_app.di.scopes.FragmentScope
import com.test.otus_film_app.view.MainActivity
import com.test.otus_film_app.view.details_screen.DetailsFragment
import com.test.otus_film_app.view.watch_later_screen.WatchLaterListFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [NotificationApiModule::class])
interface NotificationComponent {
    fun inject(detailsFragment: DetailsFragment)

    fun inject(watchLaterListFragment: WatchLaterListFragment)

    @Subcomponent.Builder
    interface Builder {
        fun notificationModule(notificationApiModule: NotificationApiModule): Builder
        fun build(): NotificationComponent
    }
}