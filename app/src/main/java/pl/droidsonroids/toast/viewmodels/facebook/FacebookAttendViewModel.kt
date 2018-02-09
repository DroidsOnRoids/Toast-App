package pl.droidsonroids.toast.viewmodels.facebook

import android.databinding.ObservableField
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.app.utils.extensions.putFacebookId
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.repositories.facebook.FacebookRepository
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.isYesterdayOrEarlier
import java.util.*
import javax.inject.Inject


class FacebookAttendViewModel @Inject constructor(
        loginStateWatcher: LoginStateWatcher,
        private val facebookRepository: FacebookRepository,
        private val firebaseAnalytics: FirebaseAnalytics
) : AttendViewModel, LoginStateWatcher by loginStateWatcher {
    override val navigationRequests: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val isPastEvent = ObservableField(false)
    override val attendStatus: ObservableField<AttendStatus> = ObservableField(AttendStatus.DECLINED)
    private var facebookAttendStateDisposable: Disposable = Disposables.disposed()
    private var facebookAttendRequestDisposable: Disposable = Disposables.disposed()
    private var facebookId: String? = null
    var date: Date? = null
    private var loginStateDisposable: Disposable = Disposables.disposed()

    @VisibleForTesting
    constructor(
            loginStateWatcher: LoginStateWatcher,
            facebookRepository: FacebookRepository,
            facebookId: String?,
            firebaseAnalytics: FirebaseAnalytics) : this(loginStateWatcher, facebookRepository, firebaseAnalytics) {
        this.facebookId = facebookId
    }

    init {
        subscribeToLoginChange()
    }


    override fun setEvent(id: String, date: Date) {
        this.facebookId = id
        this.date = date
        invalidateAttendState()
    }

    private fun subscribeToLoginChange() {
        loginStateDisposable = loginStateSubject.subscribe {
            invalidateAttendState()
        }
    }

    override fun invalidateAttendState() {
        isPastEvent.set(date?.isYesterdayOrEarlier ?: false)
        facebookId?.let {
            facebookAttendStateDisposable.dispose()
            facebookAttendStateDisposable = facebookRepository.getEventAttendState(it)
                    .subscribeBy(
                            onSuccess = { status ->
                                attendStatus.set(status)
                                firebaseAnalytics.logEvent(
                                                Constants.EventTracking.Events.ATTEND_SUCCESS,
                                                facebookId?.let { Bundle().putFacebookId(it) }
                                )
                            },
                            onError = (::onInvalidateAttendStateError)
                    )
        }
    }

    override fun onAttendClick() {
        if (!isPastEvent.get()) {
            val attendStatus = this.attendStatus.get()
            firebaseAnalytics.logEvent(
                    Constants.EventTracking.Events.ATTEND_BUTTON,
                    facebookId?.let { Bundle().putFacebookId(it) }
            )
            when {
                !hasPermissions -> navigationRequests.onNext(NavigationRequest.LogIn)
                attendStatus == AttendStatus.DECLINED -> attendOnEvent()
                else -> openFacebookEventPage()
            }
        }
    }

    private fun attendOnEvent() {
        facebookId?.let {
            facebookAttendRequestDisposable.dispose()
            facebookAttendRequestDisposable = facebookRepository.setEventAttending(it)
                    .doOnComplete { facebookAttendStateDisposable.dispose() }
                    .subscribeBy(
                            onComplete = { attendStatus.set(AttendStatus.ATTENDING) },
                            onError = (::onSetAttendingError)
                    )
        }
    }

    private fun openFacebookEventPage() {
        facebookId?.let {
            navigationRequests.onNext(NavigationRequest.Website("${Constants.Facebook.EVENT_URL}$it"))
        }
    }

    private fun onInvalidateAttendStateError(throwable: Throwable) {
        navigationRequests.onNext(NavigationRequest.SnackBar(R.string.facebook_update_attend_error))
    }

    private fun onSetAttendingError(throwable: Throwable) {
        navigationRequests.onNext(NavigationRequest.SnackBar(R.string.oops_no_internet_connection))
    }

    override fun dispose() {
        loginStateDisposable.dispose()
        facebookAttendRequestDisposable.dispose()
        facebookAttendStateDisposable.dispose()
    }
}