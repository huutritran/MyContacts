package com.example.mycontacts.domain.models

data class ContactItems(
    val contacts: List<Contact>,
    val isLastPage: Boolean = false
)

fun ContactItems.filterOutContact(withEmail: String): ContactItems {
    val filtered = this.contacts.filterNot { contact ->
        contact.email.contentEquals(withEmail)
    }
    return this.copy(contacts = filtered)
}