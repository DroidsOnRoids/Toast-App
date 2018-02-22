package pl.droidsonroids.toast.viewmodels

import io.reactivex.subjects.PublishSubject

interface RefreshViewModel {
    fun refresh()
    val swipeRefreshVisibleSubject: PublishSubject<Boolean>
}