package com.example.mycontacts.presentation.contactlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.R
import com.example.mycontacts.domain.models.Contact
import com.example.mycontacts.util.loadCircleShape

class ContactAdapter(
    private val onItemClick: (id: Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_USER = 1
        private const val TYPE_LOAD_MORE = 2
    }

    var contactList = mutableListOf<Contact>()
    var isLastPage = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_USER -> ContactViewHolder(inflater.inflate(R.layout.item_contact, parent, false))
            else -> LoadMoreViewHolder(inflater.inflate(R.layout.item_loadmore, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactViewHolder) {
            holder.bind(contactList[position])
        }
    }

    fun updateUserList(contacts: List<Contact>) {
        val callback = ContactDiffCallback(contactList, contacts)
        val result = DiffUtil.calculateDiff(callback)
        contactList = contacts.toMutableList()
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return if (isLastPage) contactList.size else contactList.size + 1
    }

//    override fun getItemCount(): Int = contactList.size

    override fun getItemViewType(position: Int): Int {
        return if (position == contactList.size) TYPE_LOAD_MORE else TYPE_USER
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgAvatar = lazy { itemView.findViewById<ImageView>(R.id.imgAvatar) }
        private val tvUserName = lazy { itemView.findViewById<TextView>(R.id.tvName) }
        private val tvEmail = lazy { itemView.findViewById<TextView>(R.id.tvEmail) }

        init {
            itemView.setOnClickListener { onItemClick(contactList[adapterPosition].id) }
        }

        fun bind(contact: Contact) {
            with(contact) {
                imgAvatar.value.loadCircleShape(contact.avatar)
                tvUserName.value.text = displayName
                tvEmail.value.text = email
            }
        }
    }

    inner class LoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}