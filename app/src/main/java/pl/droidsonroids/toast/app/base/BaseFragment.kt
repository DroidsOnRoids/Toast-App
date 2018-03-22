package pl.droidsonroids.toast.app.base

import android.content.Context
import android.support.v4.app.Fragment
import dagger.android.support.AndroidSupportInjection
import pl.droidsonroids.toast.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected val baseActivity: BaseActivity
        get() = activity as BaseActivity?
                ?: throw IllegalStateException("Tried to access forced context when not available")

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}