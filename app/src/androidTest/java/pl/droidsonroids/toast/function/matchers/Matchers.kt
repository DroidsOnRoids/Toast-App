package pl.droidsonroids.toast.function.matchers

import android.support.design.internal.BottomNavigationItemView
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher

fun isBottomNavigationItemViewChecked(): Matcher<View> {
    return object : BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {
        internal var triedMatching: Boolean = false

        override fun describeTo(description: Description) {
            if (triedMatching) {
                description.appendText("with BottomNavigationItem check status: true, But was: false")
            }
        }

        override fun matchesSafely(item: BottomNavigationItemView): Boolean {
            triedMatching = true
            return item.itemData.isChecked
        }
    }
}

fun isBottomViewElementWithCorrectTitle(title: String): Matcher<View> {
    return object : BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {
        internal var triedMatching: Boolean = false

        override fun describeTo(description: Description) {
            if (triedMatching) {
                description.appendText("with BottomNavigationItem check title: " + title)
            }
        }

        override fun matchesSafely(item: BottomNavigationItemView): Boolean {
            triedMatching = true
            return item.itemData.title == title
        }
    }
}