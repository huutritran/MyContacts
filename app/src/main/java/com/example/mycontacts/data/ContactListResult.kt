package com.example.mycontacts.data

import com.example.mycontacts.domain.models.Contact
import com.example.mycontacts.domain.models.ContactItems
import com.google.gson.annotations.SerializedName

data class ContactListResult(
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val per_page: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("data") val data: List<ContactEntity>,
)

fun ContactListResult.toContactItemsModel(): ContactItems {
    return ContactItems(
        contacts = data.map { it.toContactModel() },
        isLastPage = page == total_pages
    )
}

data class ContactEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("avatar") val avatar: String
)

fun ContactEntity.toContactModel(): Contact {
    return Contact(
        id = id,
        email = email,
        firstName = first_name,
        lastName = last_name,
        avatar = avatar
    )
}