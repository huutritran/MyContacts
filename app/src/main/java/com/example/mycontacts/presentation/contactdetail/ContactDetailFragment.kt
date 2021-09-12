package com.example.mycontacts.presentation.contactdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.R
import com.example.mycontacts.domain.models.Contact
import com.example.mycontacts.presentation.contactlist.ContactListViewModel
import com.example.mycontacts.util.loadCircleShape
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_contact_detail.*
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {
    companion object {
        const val AGR_CONTACT_ID = "AGR_CONTACT_ID"
    }

    private val viewModel: ContactListViewModel by viewModels({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(AGR_CONTACT_ID)?.let {
            viewModel.selectContact(it)
        }

        btnUpdate.setOnClickListener {
            updateContact()
        }

        viewModel.selectedContact.observe(viewLifecycleOwner, this::displayContact)
    }

    private fun updateContact() {
        viewModel.selectedContact.value?.let {
            val updatedContact = it.copy(
                email = etEmail.text.toString(),
                firstName = etFirstName.text.toString(),
                lastName = etLastName.text.toString()
            )
            val isUpdateSuccess = viewModel.updateContact(updatedContact)
            if (isUpdateSuccess) {
                findNavController().popBackStack()
            }
        }
    }

    private fun displayContact(contact: Contact?) {
        contact?.let {
            tvTitle.text = it.displayName
            tvDisplayName.text = it.displayName
            etEmail.setText(it.email)
            etFirstName.setText(it.firstName)
            etLastName.setText(it.lastName)
            imgAvatar.loadCircleShape(it.avatar)
        }
    }

}