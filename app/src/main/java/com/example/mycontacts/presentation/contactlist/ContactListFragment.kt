package com.example.mycontacts.presentation.contactlist

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.R
import com.example.mycontacts.presentation.contactdetail.ContactDetailFragment
import com.example.mycontacts.presentation.login.Authenticator
import com.example.mycontacts.util.addLoadMoreListener
import com.example.mycontacts.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.contact_list_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


@AndroidEntryPoint
class ContactListFragment : Fragment(R.layout.contact_list_fragment) {
    @Inject
    lateinit var authenticator: Authenticator
    private val viewModel: ContactListViewModel by viewModels({ requireActivity() })
    private lateinit var adapter: ContactAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.refreshContacts()
        }
        requireActivity().onBackPressedDispatcher.addCallback {
            activity?.finish()
        }
        setupToolBar()
        setupControls()
        subscribeData()
    }

    private fun setupToolBar() {
        authenticator.loggedInUser?.let {
            tvTitle.text = getString(R.string.welcome_logged_in_user,it.name)
        }
    }

    private fun setupControls() {
        rvContact.visibility = View.GONE
        adapter = ContactAdapter(::openContact)
        val linearLayoutManager =
            WrapLinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val dividerItemDecoration =
            DividerItemDecoration(this.context, linearLayoutManager.orientation)

        with(rvContact) {
            this.adapter = this@ContactListFragment.adapter
            layoutManager = linearLayoutManager
            addLoadMoreListener {
                if (shouldLoadMore()) {
                    viewModel.getContacts()
                }
            }
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun subscribeData() {
        viewModel.isLastPage.observe(viewLifecycleOwner, {
            adapter.isLastPage = it
        })
        viewModel.loading.observe(viewLifecycleOwner, { loading ->
            if (adapter.contactList.isNotEmpty()) {
                return@observe
            }
            progress_bar.visibility = if (loading) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(viewLifecycleOwner, { msg ->
            msg?.let { toast(msg) }
        })

        viewModel.contacts.observe(viewLifecycleOwner, { contacts ->
            adapter.updateUserList(contacts)
            rvContact.visibility = if (adapter.contactList.isEmpty()) View.GONE else View.VISIBLE
        })
    }

    private fun openContact(contactId: Int) {
        val bundle = bundleOf(
            ContactDetailFragment.AGR_CONTACT_ID to contactId
        )
        findNavController().navigate(R.id.goToDetail, bundle)
    }

    private fun shouldLoadMore(): Boolean {
        val isLoading = viewModel.loading.value ?: false
        return !adapter.isLastPage && !isLoading && adapter.contactList.isNotEmpty()
    }

}