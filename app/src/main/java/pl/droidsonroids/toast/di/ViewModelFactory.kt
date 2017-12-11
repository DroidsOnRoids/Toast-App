package pl.droidsonroids.toast.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(private val creators: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            creator = creators.filterKeys { modelClass.isAssignableFrom(it) }
                    .values.firstOrNull()
        }
        return creator?.get() as? T ?: throw IllegalArgumentException("unknown model class $modelClass")
    }
}