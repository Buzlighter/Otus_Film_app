package com.test.otus_film_app.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.messaging.FirebaseMessaging
import com.test.otus_film_app.App.Companion.appComponent
import com.test.otus_film_app.R
import com.test.otus_film_app.util.Constants.Companion.FROM_MAIN_ACTIVITY_NOTIFY_BUNDLE
import com.test.otus_film_app.util.Constants.Companion.TAG_REMOTE
import com.test.otus_film_app.util.CustomRemoteConfig
import com.test.otus_film_app.util.ExitDialog
import com.test.otus_film_app.util.ExitDialog.Companion.EXIT_DIALOG_TAG
import com.test.otus_film_app.util.PushService.Companion.NEW_TOKEN
import com.test.otus_film_app.view.details_screen.TOPIC
import com.test.otus_film_app.view.favorites_screen.FavoriteListFragment
import com.test.otus_film_app.view.film_list_screen.FilmListFragment
import com.test.otus_film_app.view.watch_later_screen.WatchLaterListFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val dialogExit = ExitDialog()
    @Inject
    lateinit var customRemoteConfig: CustomRemoteConfig
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

        appComponent.mainActivityComponentBuilder().create().inject(this)

        firebaseGetCurrentToken()
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

    private fun fetchRemoteData() {
        customRemoteConfig.getFirebaseConfig().fetchAndActivate()
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
        val remoteData = customRemoteConfig.getFirebaseConfig().getString("greetings")
        Log.d(TAG_REMOTE,  "RemoteData: $remoteData")
        Toast.makeText(this, remoteData, Toast.LENGTH_LONG).show()
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