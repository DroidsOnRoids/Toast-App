package pl.droidsonroids.toast.function.matchers

import android.support.design.internal.BottomNavigationItemView
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher

fun isBottomNavigationItemViewChecked(): Matcher<View> {
    return object : BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("with BottomNavigationItem checked")
        }

        override fun matchesSafely(item: BottomNavigationItemView): Boolean {
            return item.itemData.isChecked
        }
    }
}

fun isBottomViewElementWithCorrectTitle(title: String): Matcher<View> {
    return object : BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("with BottomNavigationItem title: " + title)
        }

        override fun matchesSafely(item: BottomNavigationItemView): Boolean {
            return item.itemData.title == title
        }
    }
}
