package com.example.mycontacts.data

import com.example.mycontacts.domain.models.User
import com.example.mycontacts.domain.repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun login(userName: String, password: String): Result<User> {
        if (userName == "devblock" && password == "2021") {
            return Result.success(
                User(
                    name = "Devblock Comp",
                    email = "devblock@email.com"
                )
            )
        }
        return Result.failure(
            Throwable("Incorrect username or password.")
        )
    }
}