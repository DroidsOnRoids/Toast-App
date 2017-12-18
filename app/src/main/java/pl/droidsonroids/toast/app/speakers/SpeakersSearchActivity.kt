package pl.droidsonroids.toast.app.speakers

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_speakers_search.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseActivity
import pl.droidsonroids.toast.app.utils.LazyLoadingScrollListener
import pl.droidsonroids.toast.databinding.ActivitySpeakersSearchBinding
import pl.droidsonroids.toast.viewmodels.speaker.SpeakersSearchViewModel

class SpeakersSearchActivity : BaseActivity() {
    companion object {
        fun createIntent(context: Context): Intent = Intent(context, SpeakersSearchActivity::class.java)
    }

    private val speakersSearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SpeakersSearchViewModel::class.java]
    }

    private var speakersDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val speakersSearchBinding = DataBindingUtil.setContentView<ActivitySpeakersSearchBinding>(this, R.layout.activity_speakers_search)
        setupToolbar()
        setupViewModel(speakersSearchBinding)
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel(speakersSearchBinding: ActivitySpeakersSearchBinding) {
        speakersSearchBinding.speakersSearchViewModel = speakersSearchViewModel

    }

    private fun setupRecyclerView() {
        with(speakersSearchRecyclerView) {
            val speakersAdapter = SpeakersAdapter()
            adapter = speakersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpeakerItemDecoration(context.applicationContext))
            addOnScrollListener(LazyLoadingScrollListener {
                speakersSearchViewModel.loadNextPage()
            })

            subscribeToSpeakersChange(speakersAdapter)
        }
    }

    private fun subscribeToSpeakersChange(speakersAdapter: SpeakersAdapter) {
        speakersDisposable = speakersSearchViewModel.speakersSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speakersAdapter::setData)
    }

    override fun onDestroy() {
        speakersDisposable?.dispose()
        super.onDestroy()
    }
}