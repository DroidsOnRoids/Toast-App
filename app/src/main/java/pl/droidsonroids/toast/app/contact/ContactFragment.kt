package pl.droidsonroids.toast.app.contact

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import br.com.ilhasoft.support.validation.Validator
import kotlinx.android.synthetic.main.fragment_contact.*
import pl.droidsonroids.toast.R
import pl.droidsonroids.toast.app.base.BaseFragment
import pl.droidsonroids.toast.app.utils.ContactInputFormTextWatcher
import pl.droidsonroids.toast.databinding.FragmentContactBinding
import pl.droidsonroids.toast.viewmodels.contact.ContactViewModel


class ContactFragment : BaseFragment() {

    private lateinit var contactViewModel: ContactViewModel
    private lateinit var validator: Validator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactViewModel = ViewModelProviders.of(this, viewModelFactory)[ContactViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentContactBinding.inflate(inflater, container, false)
        binding.contactViewModel = contactViewModel

        validator = Validator(binding)
        validator.setValidationListener(object : Validator.ValidationListener {
            override fun onValidationError() {}

            override fun onValidationSuccess() {}
        })
        validator.enableFormValidationMode()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopicSpinner()
        validateInputForm()
    }


    private fun validateInputForm() {
        with(contactEmailEditText) { addTextChangedListener(ContactInputFormTextWatcher(validator, this)) }
        with(contactNameEditText) { addTextChangedListener(ContactInputFormTextWatcher(validator, this)) }
        with(contactMessageEditText) { addTextChangedListener(ContactInputFormTextWatcher(validator, this)) }
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
        topicSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                (p1 as TextView).error = activity?.getString(R.string.email_address)
            }

        }
    }

}
