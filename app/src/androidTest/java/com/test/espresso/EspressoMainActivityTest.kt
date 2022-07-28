package com.test.espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.test.otus_film_app.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class EspressoMainActivityTest {

    @get:Rule
    var detailScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withText("Выйти из приложения?")).check(matches(isDisplayed()))
    }
}