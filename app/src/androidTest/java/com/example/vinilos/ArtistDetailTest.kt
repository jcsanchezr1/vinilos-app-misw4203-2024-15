package com.example.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vinilos.ui.views.HomeActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ArtistDetailTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun artistDetailTest() {
        Thread.sleep(900)

        val appCompatButton = onView(
            allOf(
                withId(R.id.visitorButton), withText("Soy visitante"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        Thread.sleep(900)

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.artists), withContentDescription("Artistas"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.artistRv),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(2, click()))

        Thread.sleep(900)

        val textView = onView(
            allOf(
                withId(R.id.tvAlbumDetailTitle), withText("Rubén Blades Bellido de Luna"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        Thread.sleep(900)
        textView.check(matches(isDisplayed()))

        Thread.sleep(900)
        val textView4 = onView(
            allOf(
                withId(R.id.tvAlbumes), withText("Álbumes"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Álbumes")))

        Thread.sleep(900)
        val textView5 = onView(
            allOf(
                withId(R.id.tvAlbumes), withText("Álbumes"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Álbumes")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
