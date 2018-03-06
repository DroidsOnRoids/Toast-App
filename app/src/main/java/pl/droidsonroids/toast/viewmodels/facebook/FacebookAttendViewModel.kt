package pl.droidsonroids.toast.viewmodels.facebook

import android.databinding.ObservableField
import android.support.annotation.VisibleForTesting
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.facebook.LoginStateWatcher
import pl.droidsonroids.toast.app.utils.managers.AnalyticsEventTracker
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.repositories.facebook.FacebookRepository
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.SourceAttending
import pl.droidsonroids.toast.utils.isYesterdayOrEarlier
import java.util.*
import javax.inject.Inject

// Not used for now due to graph API change - removed possibility to check attend status
class FacebookAttendViewModel @Inject constructor(
        loginStateWatcher: LoginStateWatcher,
        private val facebookRepository: FacebookRepository,
        private val analyticsEventTracker: AnalyticsEventTracker
) : AttendViewModel, LoginStateWatcher by loginStateWatcher {
    override val navigationRequests: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val isPastEvent = ObservableField(false)
    override val attendStatus: ObservableField<AttendStatus> = ObservableField(AttendStatus.DECLINED)
    private var facebookAttendStateDisposable: Disposable = Disposables.disposed()
    private var facebookAttendRequestDisposable: Disposable = Disposables.disposed()
    private var facebookId: String? = null
    var date: Date? = null
    private var loginStateDisposable: Disposable = Disposables.disposed()
    private var sourceAttending = SourceAttending.UPCOMING_EVENT

    @VisibleForTesting
    constructor(
            loginStateWatcher: LoginStateWatcher,
            facebookRepository: FacebookRepository,
            analyticsEventTracker: AnalyticsEventTracker,
            facebookId: String?
    ) : this(loginStateWatcher, facebookRepository, analyticsEventTracker) {
        this.facebookId = facebookId
    }

    init {
        subscribeToLoginChange()
    }


    override fun setEvent(id: String, date: Date, sourceAttending: SourceAttending) {
        this.facebookId = id
        this.date = date
        this.sourceAttending = sourceAttending
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
                    .subscribe(::onUpdateAttendStatus)
        }
    }

    private fun onUpdateAttendStatus(status: AttendStatus) {
        when (status) {
            AttendStatus.ERROR -> onInvalidateAttendStateError()
            else -> attendStatus.set(status)
        }
    }

    private fun onInvalidateAttendStateError() {
        navigationRequests.onNext(NavigationRequest.SnackBar(R.string.facebook_update_attend_error))
    }

    override fun onAttendClick() {
        if (!isPastEvent.get()) {
            val attendStatus = this.attendStatus.get()
            when {
                !hasPermissions -> navigationRequests.onNext(NavigationRequest.LogIn)
                attendStatus == AttendStatus.DECLINED -> attendOnEvent()
                else -> openFacebookEventPage()
            }
            logFacebookAttendEvent()
        }
    }

    private fun attendOnEvent() {
        facebookId?.let {
            facebookAttendRequestDisposable.dispose()
            facebookAttendRequestDisposable = facebookRepository.setEventAttending(it)
                    .doOnComplete { facebookAttendStateDisposable.dispose() }
                    .subscribeBy(
                            onComplete = {
                                attendStatus.set(AttendStatus.ATTENDING)
                                logFacebookAttendSuccessEvent()
                            },
                            onError = (::onSetAttendingError)
                    )
        }
    }

    private fun openFacebookEventPage() {
        facebookId?.let {
            navigationRequests.onNext(NavigationRequest.Website("${Constants.Facebook.EVENT_URL}$it"))
        }
    }

    private fun onSetAttendingError(throwable: Throwable) {
        navigationRequests.onNext(NavigationRequest.SnackBar(R.string.oops_no_internet_connection))
    }

    private fun logFacebookAttendEvent() {
        facebookId?.let {
            if (sourceAttending == SourceAttending.UPCOMING_EVENT) {
                analyticsEventTracker.logUpcomingEventFacebookAttendEvent(it)
            } else {
                analyticsEventTracker.logEventDetailsFacebookAttendEvent(it)
            }
        }
    }

    private fun logFacebookAttendSuccessEvent() {
        facebookId?.let {
            if (sourceAttending == SourceAttending.UPCOMING_EVENT) {
                analyticsEventTracker.logUpcomingEventFacebookAttendSuccessEvent(it)
            } else {
                analyticsEventTracker.logEventDetailsFacebookAttendSuccessEvent(it)
            }
        }
    }

    override fun dispose() {
        loginStateDisposable.dispose()
        facebookAttendRequestDisposable.dispose()
        facebookAttendStateDisposable.dispose()
    }
}