package com.test.otus_film_app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.otus_film_app.R
import com.test.otus_film_app.util.ExitDialog
import com.test.otus_film_app.util.ExitDialog.Companion.EXIT_DIALOG_TAG
import com.test.otus_film_app.view.favorites_screen.FavoriteListFragment
import com.test.otus_film_app.view.film_list_screen.FilmListFragment

class MainActivity : AppCompatActivity() {
    private val dialogExit = ExitDialog()
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace<FilmListFragment>(R.id.fragment_main_container)
                setReorderingAllowed(true)
            }
        }

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_menu_favorites -> {
                    supportFragmentManager.commit {
                        replace<FavoriteListFragment>(R.id.fragment_main_container)
                        setReorderingAllowed(true)
                    }
                    true
                }
                else -> {
                    supportFragmentManager.commit {
                        replace<FilmListFragment>(R.id.fragment_main_container)
                        setReorderingAllowed(true)
                    }
                    true
                }
            }
        }
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            dialogExit.show(supportFragmentManager, EXIT_DIALOG_TAG)
        } else {
            super.onBackPressed()
        }
    }
}