package pl.droidsonroids.toast.viewmodels

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.utils.NavigationRequest
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel(), NavigatingViewModel {
    override val navigationSubject: PublishSubject<NavigationRequest> = PublishSubject.create()

    fun onSpeakerSearchRequested() {
        navigationSubject.onNext(NavigationRequest.SpeakersSearch)
    }
}