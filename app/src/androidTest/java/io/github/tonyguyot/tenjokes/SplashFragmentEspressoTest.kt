package io.github.tonyguyot.tenjokes

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.github.tonyguyot.tenjokes.ui.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class SplashFragmentEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun appCounterIsDisplayed() {
        // check that the app counter is displayed in the splash screen
        onView(withId(R.id.appStartCounter)).check(matches(isDisplayed()))
    }

    @Test
    fun transitionIsDone() {
        // Check that at the end of the timer, the main fragment is displayed
        // Note: we usually do not perform sleep actions in espresso tests, but here
        //       we precisely want to check the timer
        Thread.sleep(3500L)
        onView(withId(R.id.swipeRefreshJokeList)).check(matches(isDisplayed()))
        onView(withId(R.id.appStartCounter)).check(doesNotExist())
    }
}