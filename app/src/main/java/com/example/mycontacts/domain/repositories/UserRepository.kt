package com.example.mycontacts.domain.repositories

import com.example.mycontacts.domain.models.User

interface UserRepository {
    suspend fun login(userName: String, password: String) : Result<User>
}