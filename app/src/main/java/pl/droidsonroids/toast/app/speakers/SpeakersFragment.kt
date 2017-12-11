package pl.droidsonroids.toast.app.speakers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseFragment

class SpeakersFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_speakers, container, false)
}