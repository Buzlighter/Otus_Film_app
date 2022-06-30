package com.test.otus_film_app.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.test.otus_film_app.R
import com.test.otus_film_app.util.Constants.Companion.FROM_MAIN_ACTIVITY_NOTIFY_BUNDLE
import com.test.otus_film_app.util.Constants.Companion.REMOTE_BUNDLE
import com.test.otus_film_app.util.Constants.Companion.TAG_REMOTE
import com.test.otus_film_app.util.ExitDialog
import com.test.otus_film_app.util.ExitDialog.Companion.EXIT_DIALOG_TAG
import com.test.otus_film_app.util.PushService.Companion.NEW_TOKEN
import com.test.otus_film_app.view.details_screen.TOPIC
import com.test.otus_film_app.view.favorites_screen.FavoriteListFragment
import com.test.otus_film_app.view.film_list_screen.FilmListFragment
import com.test.otus_film_app.view.watch_later_screen.WatchLaterListFragment
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor


class MainActivity : AppCompatActivity() {
    private val dialogExit = ExitDialog()
    lateinit var remoteConfig: FirebaseRemoteConfig
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)

        val notificationBundle = intent.extras

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace<FilmListFragment>(R.id.fragment_main_container, FROM_MAIN_ACTIVITY_NOTIFY_BUNDLE, notificationBundle)
                setReorderingAllowed(true)
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        bottomNavigationView.setOnItemSelectedListener(onNavigationFilmMenuSelected)
        bottomNavigationView.setOnItemReselectedListener {}

        firebaseGetCurrentToken()

        fireBaseSetRemoteConfig()
        fetchRemoteData()
    }

    private val onNavigationFilmMenuSelected = NavigationBarView.OnItemSelectedListener {
        when (it.itemId) {
            R.id.item_menu_home -> {
                supportFragmentManager.commit {
                    replace<FilmListFragment>(R.id.fragment_main_container)
                    setReorderingAllowed(true)
                }
                true
            }
            R.id.item_menu_favorites -> {
                supportFragmentManager.commit {
                    replace<FavoriteListFragment>(R.id.fragment_main_container)
                    setReorderingAllowed(true)
                }
                true
            }
            else -> {
                supportFragmentManager.commit {
                    replace<WatchLaterListFragment>(R.id.fragment_main_container)
                    setReorderingAllowed(true)
                }
                true
            }
        }
    }

    private fun firebaseGetCurrentToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(NEW_TOKEN, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token ?: "fail"
            Log.d(NEW_TOKEN, msg)
        })
    }

    private fun fireBaseSetRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)

        Log.d(TAG_REMOTE, "start")
    }


    private fun fetchRemoteData() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG_REMOTE, "Config params updated: $updated")
                    getData()
                } else {
                    Log.d(TAG_REMOTE, "remote config fail")
                }
            }
    }

    private fun getData() {
//        val fragment = supportFragmentManager.findFragmentByTag(FROM_MAIN_ACTIVITY_NOTIFY_BUNDLE)
        val remoteData = remoteConfig.getString("greetings")
        Log.d(TAG_REMOTE,  "RemoteData: $remoteData")
        Toast.makeText(this, remoteData, Toast.LENGTH_LONG).show()
//        fragment?.arguments = bundleOf(Pair(REMOTE_BUNDLE, remoteData))
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            dialogExit.show(supportFragmentManager, EXIT_DIALOG_TAG)
        } else {
            super.onBackPressed()
        }
    }
}