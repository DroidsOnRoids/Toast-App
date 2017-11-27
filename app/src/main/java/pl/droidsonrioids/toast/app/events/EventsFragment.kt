package pl.droidsonrioids.toast.app.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.app.base.BaseFragment
import pl.droidsonrioids.toast.viewmodels.EventsViewModel

class EventsFragment : BaseFragment() {

    private lateinit var eventsViewModel: EventsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventsViewModel = ViewModelProviders.of(this, viewModelFactory)[EventsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_events, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
