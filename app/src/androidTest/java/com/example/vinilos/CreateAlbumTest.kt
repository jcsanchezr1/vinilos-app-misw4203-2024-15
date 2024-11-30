package com.example.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vinilos.ui.views.HomeActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateAlbumTestTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun createAlbumTest() {
        val appCompatButton = onView(
            allOf(
                withId(R.id.collectorButton), withText("Soy coleccionista"),
                childAtPosition(
                    allOf(
                        withId(R.id.main),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val appCompatImageView = onView(
            allOf(
                withId(R.id.ivAdd), withContentDescription("Crear álbum"),
                childAtPosition(
                    allOf(
                        withId(R.id.header_layout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.etAlbumName),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("test"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.etAlbumCover),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    6
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("test"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.etAlbumReleaseDate),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    8
                )
            )
        )
        appCompatEditText3.perform(scrollTo(), click())

        onView(withText("OK"))
            .inRoot(isDialog())
            .perform(click())


        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.etAlbumDescription),
                childAtPosition(
                    allOf(
                        withId(R.id.flDescriptoin),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            10
                        )
                    ),
                    0
                )
            )
        )
        appCompatEditText4.perform(scrollTo(), replaceText("test"), closeSoftKeyboard())

        // Select genre
        onView(withId(R.id.spinnerGenre))
            .perform(scrollTo(), click())

        onData(anything())
            .inRoot(RootMatchers.isPlatformPopup())
            .atPosition(1)
            .perform(click())

// Select record label
        onView(withId(R.id.spinnerRecordLabel))
            .perform(scrollTo(), click())

        onData(anything())
            .inRoot(RootMatchers.isPlatformPopup())
            .atPosition(1)
            .perform(click())


        val appCompatButton3 = onView(
            allOf(
                withId(R.id.btnSubmitAlbum), withText("Crear álbum"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    15
                )
            )
        )
        appCompatButton3.perform(scrollTo(), click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.dialogButton), withText("ACEPTAR"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())
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
