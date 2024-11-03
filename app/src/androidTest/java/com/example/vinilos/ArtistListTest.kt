package com.example.vinilos

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vinilos.ui.views.HomeActivity
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ArtistListTest {

    @get:Rule
    val homeActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun listArtistSuccess() {
        onView(withId(R.id.visitorButton)).perform(
            click()
        )
        onView(withId(R.id.artists)).perform(
            click()
        )

        onView(allOf(withId(R.id.artistTitle), withText("Artistas"))).check(matches(isDisplayed()))
    }

    @Test
    fun listBandsSuccess() {
        onView(withId(R.id.visitorButton)).perform(
            click()
        )
        onView(withId(R.id.artists)).perform(
            click()
        )

        onView(withId(R.id.bandsButton)).perform(
            click()
        )

        onView(allOf(withId(R.id.artistTitle), withText("Artistas"))).check(matches(isDisplayed()))
        onView(withId(R.id.artistRv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(hasDescendant(allOf(withId(R.id.frameArtist), isDisplayed()))))
    }

    @Test
    fun listMusiciansSuccess() {
        onView(withId(R.id.visitorButton)).perform(
            click()
        )
        onView(withId(R.id.artists)).perform(
            click()
        )

        onView(withId(R.id.bandsButton)).perform(
            click()
        )

        onView(withId(R.id.musiciansButton)).perform(
            click()
        )

        onView(allOf(withId(R.id.artistTitle), withText("Artistas"))).check(matches(isDisplayed()))
        onView(withId(R.id.artistRv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(hasDescendant(allOf(withId(R.id.frameArtist), isDisplayed()))))
    }
}