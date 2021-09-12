package com.example.mycontacts.domain.models

data class Contact(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String
) {
    val displayName: String get() = "$firstName $lastName"
}