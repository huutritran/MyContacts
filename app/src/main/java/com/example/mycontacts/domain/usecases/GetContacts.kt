package com.example.mycontacts.domain.usecases

import com.example.mycontacts.di.IODispatcher
import com.example.mycontacts.di.MainDispatcher
import com.example.mycontacts.domain.models.ContactItems
import com.example.mycontacts.domain.models.filterOutContact
import com.example.mycontacts.domain.repositories.ContactRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetContacts @Inject constructor(
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val contactRepository: ContactRepository
) : UseCase<ContactItems, GetContacts.Params>(
    mainDispatcher,
    ioDispatcher
) {
    data class Params(
        val page: Int,
        val getEmailFilterOut: () -> String = { "janet.weaver@reqres.in" }
    )

    override suspend fun run(params: Params): Result<ContactItems> {
        return contactRepository.getContact(page = params.page).let { result ->
            val contactItems = result.getOrNull()
            if (result.isSuccess && contactItems != null) {
                val emailShouldRemove = params.getEmailFilterOut()
                return@let Result.success(contactItems.filterOutContact(emailShouldRemove))
            }
            return@let result
        }
    }
}