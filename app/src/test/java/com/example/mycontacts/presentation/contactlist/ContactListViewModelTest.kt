package com.example.mycontacts.presentation.contactlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mycontacts.domain.models.Contact
import com.example.mycontacts.domain.models.ContactItems
import com.example.mycontacts.domain.models.filterOutContact
import com.example.mycontacts.domain.repositories.ContactRepository
import com.example.mycontacts.domain.usecases.GetContacts
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContactListViewModelTest {
    private lateinit var contactRepository: ContactRepository
    private lateinit var getContactsUseCase: GetContacts
    private lateinit var viewModel: ContactListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        contactRepository = mockk(relaxed = true)
        getContactsUseCase = GetContacts(
            Dispatchers.Unconfined,
            Dispatchers.Unconfined,
            contactRepository
        )
        viewModel = ContactListViewModel(getContactsUseCase)
    }

    @Test
    fun `getContacts should returns correctly when success`() {
        //given
        val getEmailFilterOut: () -> String = { "test@gmail.com" }
        val params = GetContacts.Params(
            page = 1,
            getEmailFilterOut,
        )
        coEvery { getContactsUseCase.run(params) } returns Result.success(
            ContactItems(
                listOf(contact),
                true
            )
        )
        val loadingObserver = mockk<Observer<Boolean>> { every { onChanged(any()) } just Runs }
        viewModel.loading.observeForever(loadingObserver)

        //when
        runBlocking { viewModel.getContacts() }

        //then
        verifySequence {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        viewModel.isLastPage.value shouldBe true
        viewModel.contacts.value shouldBe listOf(contact)
        viewModel.error.value shouldBe null
        coVerify { getContactsUseCase.run(params) }
    }

    @Test
    fun `getContacts should returns error correctly when fail`() {
        //given
        mockkStatic(ContactItems::filterOutContact)
        coEvery { contactRepository.getContact(any()) } returns Result.failure(error)
        val loadingObserver = mockk<Observer<Boolean>> { every { onChanged(any()) } just Runs }
        viewModel.loading.observeForever(loadingObserver)

        val errorObserver = mockk<Observer<String?>> { every { onChanged(any()) } just Runs }
        viewModel.error.observeForever(errorObserver)

        val isLastPage = mockk<Observer<Boolean>> { every { onChanged(any()) } just Runs }
        viewModel.isLastPage.observeForever(isLastPage)

        val contactObserver =
            mockk<Observer<List<Contact>>> { every { onChanged(any()) } just Runs }
        viewModel.contacts.observeForever(contactObserver)

        //when
        runBlocking { viewModel.getContacts() }

        //then
        verifySequence {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        verify(exactly = 0) {
            isLastPage.onChanged(any())
        }
        verify(exactly = 0) {
            contactObserver.onChanged(any())
        }
        verify {
            errorObserver.onChanged(error.message)
        }
        coVerify {
            contactRepository.getContact(any())
        }

    }

    @Test
    fun `selectContact should notify data correctly`() {
        //given
        val getEmailFilterOut: () -> String = { "test@gmail.com" }
        val params = GetContacts.Params(
            page = 1,
            getEmailFilterOut,
        )
        val selectedContactObserver = mockk<Observer<Contact?>> {
            every { onChanged(any()) } just
                    Runs
        }.also { viewModel.selectedContact.observeForever(it) }

        coEvery { getContactsUseCase.run(params) } returns Result.success(
            ContactItems(
                listOf(contact),
                true
            )
        )

        //when
        viewModel.getContacts()
        viewModel.selectContact(contact.id)

        //then
        verify {
            selectedContactObserver.onChanged(contact)
        }
    }

    @Test
    fun `updateContact should notify data correctly`() {
        //given
        val updatedContact = contact.copy(
            firstName = "UpdatedFirstName", lastName =
            "UpdatedLastName"
        )
        val getEmailFilterOut: () -> String = { "test@gmail.com" }
        val params = GetContacts.Params(
            page = 1,
            getEmailFilterOut,
        )

        coEvery { getContactsUseCase.run(params) } returns Result.success(
            ContactItems(
                listOf(contact),
                true
            )
        )

        val contactObserver =
            mockk<Observer<List<Contact>>> { every { onChanged(any()) } just Runs }.also {
                viewModel.contacts.observeForever(it)
            }

        //when
        viewModel.getContacts()
        val result = viewModel.updateContact(updatedContact)

        //then
        result shouldBe true
        verifySequence {
            contactObserver.onChanged(listOf(contact))
            contactObserver.onChanged(listOf(updatedContact))
        }
    }

    companion object {
        val contact = Contact(
            id = 1,
            email = "email@gmail.com",
            lastName = "lastName",
            firstName = "firstName",
            avatar = "avatar"
        )
        val error = Error("Error message")
    }
}