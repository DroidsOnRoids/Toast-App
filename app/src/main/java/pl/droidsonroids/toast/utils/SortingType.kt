package pl.droidsonroids.toast.utils


enum class SortingType {
    ALPHABETICAL, DATE;

    fun toQuery(): String =
            if (this == SortingType.ALPHABETICAL) {
                Constants.SortingQuery.ALPHABETICAL
            } else {
                Constants.SortingQuery.DATE
            }
}


