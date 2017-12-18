package pl.droidsonroids.toast.viewmodels

import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.utils.NavigationRequest

interface NavigatingViewModel {
    val navigationSubject: PublishSubject<NavigationRequest>
}