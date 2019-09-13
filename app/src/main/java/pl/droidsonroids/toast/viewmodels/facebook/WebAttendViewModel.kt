package pl.droidsonroids.toast.viewmodels.facebook

import android.databinding.ObservableField
import io.reactivex.subjects.PublishSubject
import pl.droidsonroids.toast.data.enums.AttendStatus
import pl.droidsonroids.toast.utils.Constants
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.utils.SourceAttending
import pl.droidsonroids.toast.utils.isYesterdayOrEarlier
import java.util.*

class WebAttendViewModel : AttendViewModel {
    override val attendStatus = ObservableField(AttendStatus.DECLINED)
    override val navigationRequests: PublishSubject<NavigationRequest> = PublishSubject.create()
    override val isPastEvent = ObservableField(false)

    private var eventDate: Date? = null
    private var facebookId: String? = null

    override fun setEvent(id: String, date: Date, sourceAttending: SourceAttending) {
        eventDate = date
        facebookId = id
        invalidateAttendState()
    }

    override fun invalidateAttendState() {
        isPastEvent.set(eventDate?.isYesterdayOrEarlier ?: false)
    }

    override fun onAttendClick() {
        if (!isPastEvent.get()!! && facebookId != null) {
            navigationRequests.onNext(NavigationRequest.Website("${Constants.Facebook.EVENT_URL}$facebookId"))
        }
    }

    override fun dispose() = Unit
}