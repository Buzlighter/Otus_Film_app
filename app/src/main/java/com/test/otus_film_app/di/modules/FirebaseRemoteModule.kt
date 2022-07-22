package com.test.otus_film_app.di.modules

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.test.otus_film_app.R
import com.test.otus_film_app.di.scopes.ActivityScope
import com.test.otus_film_app.util.Constants
import dagger.Module
import dagger.Provides

@Module
object FirebaseRemoteModule {

    @ActivityScope
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_default)
        }
    }
}