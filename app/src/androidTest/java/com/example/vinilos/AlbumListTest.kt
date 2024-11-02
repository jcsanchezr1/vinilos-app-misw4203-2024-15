package com.example.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
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
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AlbumListTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun openAlbumSuccess(){
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

        val textView = onView(
            allOf(
                withId(R.id.tvTitle), withText("Álbumes"),
                withParent(withParent(withId(R.id.frame_layout))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Álbumes")))
    }

    @Test
    fun searchAlbumSuccessTest() {

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

        val appCompatEditText = onView(
            allOf(
                withId(R.id.searchBar), withText(""),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frame_layout),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("A Day"))

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.searchBar), withText("A Day"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frame_layout),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(closeSoftKeyboard())

        Thread.sleep(2000)

        val linearLayout = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.albumRv),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )


        linearLayout.check(matches(isDisplayed()))


    }
    @Test
    fun searchAlbumNoSuccessTest() {
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


        val appCompatEditText = onView(
            allOf(
                withId(R.id.searchBar), withText(""),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frame_layout),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("white"))

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.searchBar), withText("white"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frame_layout),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(closeSoftKeyboard())

        val textView2 = onView(
            allOf(
                withId(R.id.tvNoResults),
                withText("No se encontraron álbumes que coincidan con tu búsqueda"),
                withParent(withParent(withId(R.id.frame_layout))),
                isDisplayed()
            )
        )

        Thread.sleep(2000)
        textView2.check(matches(withText("No se encontraron álbumes que coincidan con tu búsqueda")))
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
