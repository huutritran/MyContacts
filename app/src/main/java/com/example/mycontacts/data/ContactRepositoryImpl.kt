package com.example.mycontacts.data

import com.example.mycontacts.domain.models.ContactItems
import com.example.mycontacts.domain.repositories.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val contactsApi: ContactsApi
) : ContactRepository {

    override suspend fun getContact(page: Int): Result<ContactItems> {
        return kotlin.runCatching {
            contactsApi.getContacts(page).toContactItemsModel().let {
                Result.success(it)
            }
        }.getOrElse {
            Result.failure(it)
        }
    }

}