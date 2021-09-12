package com.example.mycontacts.domain.repositories

import com.example.mycontacts.domain.models.ContactItems

interface ContactRepository {
    suspend fun getContact(page: Int): Result<ContactItems>
}