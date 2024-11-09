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
    fun openAlbumSuccess() {
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
                isDisplayed()
            )
        )
        textView.check(matches(withText("Álbumes")))
    }

    @Test
    fun searchAlbumSuccessTest() {
        val visitorButton = onView(
            allOf(
                withId(R.id.visitorButton), withText("Soy visitante"),
                isDisplayed()
            )
        )
        visitorButton.perform(click())

        // Esperar a que la búsqueda se cargue
        val searchBar = onView(
            allOf(
                withId(R.id.searchBar),
                isDisplayed()
            )
        )
        Thread.sleep(3000)

        searchBar.perform(replaceText("A Day"))
        searchBar.perform(closeSoftKeyboard())

        Thread.sleep(3000)

        // Verificar que la lista de álbumes aparece después de la búsqueda
        val albumList = onView(
            allOf(
                withId(R.id.albumRv), // Actualizar a tu RecyclerView de álbumes
                isDisplayed()
            )
        )
        albumList.check(matches(isDisplayed()))

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
        val visitorButton = onView(
            allOf(
                withId(R.id.visitorButton), withText("Soy visitante"),
                isDisplayed()
            )
        )
        visitorButton.perform(click())

        val searchBar = onView(
            allOf(
                withId(R.id.searchBar),
                isDisplayed()
            )
        )
        searchBar.perform(replaceText("white"))
        searchBar.perform(closeSoftKeyboard())

        // Verificar que el mensaje de "No se encontraron resultados" aparece
        val noResultsText = onView(
            allOf(
                withId(R.id.tvNoResults),
                withText("No se encontraron álbumes que coincidan con tu búsqueda"),
                isDisplayed()
            )
        )
        noResultsText.check(matches(withText("No se encontraron álbumes que coincidan con tu búsqueda")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
