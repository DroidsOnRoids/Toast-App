package pl.droidsonroids.toast.function.matchers

import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.TextInputLayout
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import android.widget.EditText
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

fun isHintOnEditTextCorrect(hint: String): Matcher<View> {
    return object : BoundedMatcher<View, EditText>(EditText::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("with EditText hint: " + hint)
        }

        override fun matchesSafely(editText: EditText): Boolean {
            return editText.hint == hint
        }
    }
}

fun isHintOnTextInputLayoutCorrect(hint: String): Matcher<View> {
    return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("with TextInputLayout hint: " + hint)
        }

        override fun matchesSafely(textInputLayout: TextInputLayout): Boolean {
            return textInputLayout.hint == hint
        }
    }
}

fun isErrorOnTextInputLayoutCorrect(error: String?): Matcher<View> {
    return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("with TextInputLayout error: " + error)
        }

        override fun matchesSafely(textInputLayout: TextInputLayout): Boolean {
            return textInputLayout.error == error && textInputLayout.isErrorEnabled
        }
    }
}