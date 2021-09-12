package com.example.mycontacts.presentation.contactlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mycontacts.domain.models.Contact
import com.example.mycontacts.domain.models.ContactItems
import com.example.mycontacts.domain.usecases.GetContacts
import com.example.mycontacts.presentation.base.BaseViewModel
import com.example.mycontacts.util.addAllAndNotify
import com.example.mycontacts.util.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val getContactsUseCase: GetContacts
) : BaseViewModel() {
    val contacts: LiveData<List<Contact>>
        get() = _contacts
    private val _contacts = MutableLiveData<List<Contact>>()

    private var pageNumber = 1
    val isLastPage = MutableLiveData<Boolean>()

    val selectedContact: LiveData<Contact?>
        get() = _selectedContact
    private val _selectedContact = MutableLiveData<Contact?>()

    fun updateContact(contact: Contact): Boolean {
        if (contact.email.isEmpty() || contact.firstName.isEmpty() || contact.displayName.isEmpty()) {
            onFailure(Throwable("Information should not be blank"))
            return false
        }
        _selectedContact.value = contact
        _contacts.value?.map { it.id }?.indexOf(contact.id)?.let {
            val updatedList = _contacts.value!!.update(it, contact)
            _contacts.value = updatedList
        }
        return true
    }

    fun selectContact(id: Int) {
        _selectedContact.value = contacts.value?.find { it.id == id }
    }

    fun refreshContacts() {
        pageNumber = 1
        isLastPage.value = false
        _contacts.value = emptyList()
        getContacts()
    }

    fun getContacts() {
        setLoading(true)
        getContactsUseCase(GetContacts.Params(pageNumber), viewModelScope) {
            setLoading(false)
            it.fold(::onGetContactsSuccess, ::onFailure)
        }
    }

    private fun onGetContactsSuccess(contactItems: ContactItems) {
        isLastPage.value = contactItems.isLastPage
        if (!contactItems.isLastPage) {
            pageNumber += 1
        }
        _contacts.addAllAndNotify(contactItems.contacts)
    }

    override fun onCleared() {
        clearError()
    }
}