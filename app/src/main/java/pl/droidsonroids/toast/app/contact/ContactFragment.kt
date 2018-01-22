package pl.droidsonroids.toast.app.contact

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.fragment_contact.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.Navigator
import pl.droidsonroids.toast.app.base.BaseFragment
import pl.droidsonroids.toast.databinding.FragmentContactBinding
import pl.droidsonroids.toast.utils.NavigationRequest
import pl.droidsonroids.toast.viewmodels.contact.ContactViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ContactFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    private lateinit var contactViewModel: ContactViewModel

    private val forcedContext: Context get() = context ?: throw IllegalStateException("Tried to access forced context when not available")

    private var navigationDisposable: Disposable = Disposables.disposed()

    private var dialogTimerDisposable: Disposable = Disposables.disposed()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactViewModel = ViewModelProviders.of(this, viewModelFactory)[ContactViewModel::class.java]
        navigationDisposable = contactViewModel.navigationSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::handleNavigationRequest)
    }

    private fun handleNavigationRequest(navigationRequest: NavigationRequest) {
        if (navigationRequest is NavigationRequest.MessageSent) {
            showMessageSentDialog()
        } else {
            navigator.dispatch(forcedContext, navigationRequest)
        }
    }

    private fun showMessageSentDialog() {
        AlertDialog.Builder(context)
                .setView(R.layout.layout_message_sent)
                .setOnDismissListener { dialogTimerDisposable.dispose() }
                .create()
                .run {
                    dialogTimerDisposable = startDismissTimer(this)
                    show()
                }
    }

    private fun startDismissTimer(dialog: AlertDialog): Disposable {
        return Completable.timer(2L, TimeUnit.SECONDS)
                .subscribe(dialog::dismiss)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentContactBinding.inflate(inflater, container, false)
        binding.contactViewModel = contactViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopicSpinner()
        setupMessageTextBox()
    }

    private fun setupTopicSpinner() {
        val adapter = object : ArrayAdapter<String>(context, R.layout.item_contact_spinner, resources.getStringArray(R.array.contact_topics)) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val dropDownView = super.getDropDownView(position, convertView, parent)
                val isHintItem = position == 0
                dropDownView.isClickable = isHintItem
                dropDownView.isEnabled = !isHintItem
                return dropDownView
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        topicSpinner.adapter = adapter
        topicSpinner.setOnLongClickListener { false }
    }

    private fun setupMessageTextBox() {
        val layoutListener: View.OnLayoutChangeListener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            if (v.hasFocus()) scrollToMessage()
        }
        contactMessageEditText.addOnLayoutChangeListener(layoutListener)
        contactMessageEditText.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) scrollToMessage() }
    }

    private fun scrollToMessage() {
        contactScrollContainer.smoothScrollTo(0, contactMessageInputLayout.top)
    }

    override fun onStop() {
        contactViewModel.saveMessage()
        super.onStop()
    }

    override fun onDetach() {
        navigationDisposable.dispose()
        super.onDetach()
    }

    override fun onDestroyView() {
        dialogTimerDisposable.dispose()
        super.onDestroyView()
    }
}
