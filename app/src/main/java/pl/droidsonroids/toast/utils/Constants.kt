package pl.droidsonroids.toast.utils

object Constants {
    object SearchMenuItem {
        const val SHOWN_OFFSET = 0f
        const val HIDDEN_OFFSET = -200f
        const val ANIM_DURATION_MILLIS = 200L
    }

    object Page {
        const val SIZE = 10
        const val FIRST = 1
    }

    object Date{
        const val PATTERN = "dd.MM.yyyy"
    }

    object ValidationRegex{
        const val EMAIL = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$"
        const val NAME = "^[a-zA-Z]+[\\-'\\s]?[a-zA-Z ]+\$"
    }

}