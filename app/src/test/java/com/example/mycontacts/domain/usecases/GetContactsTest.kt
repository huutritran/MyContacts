package com.example.mycontacts.domain.usecases

import com.example.mycontacts.domain.models.Contact
import com.example.mycontacts.domain.models.ContactItems
import com.example.mycontacts.domain.repositories.ContactRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test

class GetContactsTest : BaseTest() {
    private lateinit var contactRepository: ContactRepository
    private lateinit var getContactsUseCase: GetContacts

    override fun setup() {
        super.setup()
        contactRepository = mockk(relaxed = true)
        getContactsUseCase = GetContacts(
            mainDispatcher,
            ioDispatcher,
            contactRepository
        )
    }

    @Test
    fun `getContactsUseCase should return error`() {
        //given
        val filteredEmail: () -> String = { "test@email.com" }
        val param = GetContacts.Params(
            1,
            filteredEmail
        )
        var result: Result<ContactItems>? = null
        coEvery { contactRepository.getContact(any()) } returns Result.failure(error)


        //when
        getContactsUseCase(param, scope) { result = it }

        //then
        result shouldNotBe null
        result!!.isFailure shouldBe true
        result!!.exceptionOrNull() shouldBe error
        coVerify { contactRepository.getContact(page = param.page) }
    }

    @Test
    fun `getContactsUseCase should return correct value when success`() {
        //given
        val filteredEmail: () -> String = { contact.email }
        val param = GetContacts.Params(
            1,
            filteredEmail
        )
        var result: Result<ContactItems>? = null
        coEvery { contactRepository.getContact(any()) } returns Result.success(contactItems)


        //when
        getContactsUseCase(param, scope) { result = it }

        //then
        result shouldNotBe null
        result!!.isFailure shouldBe false
        result!!.getOrNull() shouldBe ContactItems(
            isLastPage = true,
            contacts = listOf(contactSecond)
        )
        coEvery { contactRepository.getContact(page = param.page) }
    }

    companion object {
        val error = Error("error message")
        val contact = Contact(
            id = 1,
            email = "contact1@email.com",
            firstName = "firstName",
            lastName = "lastName",
            avatar = "avatar"
        )
        val contactSecond = contact.copy(
            id = 2,
            email = "contactSecond@email.com"
        )

        val contactItems = ContactItems(
            contacts = listOf(contact, contactSecond),
            isLastPage = true
        )
    }
}