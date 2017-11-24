package pl.droidsonrioids.toast.app.dummy

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import pl.droidsonrioids.toast.app.base.BaseFragment
import pl.droidsonrioids.toast.viewmodels.DummyViewModel

class DummyFragment : BaseFragment() {

    private lateinit var dummyViewModel: DummyViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dummyViewModel = ViewModelProviders.of(this, viewModelFactory)[DummyViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            TextView(context, null, 0).apply {
                text = dummyViewModel.text
            }
}