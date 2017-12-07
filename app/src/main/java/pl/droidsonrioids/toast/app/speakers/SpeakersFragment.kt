package pl.droidsonrioids.toast.app.speakers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.droidsonrioids.toast.R
import pl.droidsonrioids.toast.app.base.BaseFragment

class SpeakersFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_speakers, container, false)
}