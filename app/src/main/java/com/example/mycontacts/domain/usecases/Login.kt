package com.example.mycontacts.domain.usecases

import com.example.mycontacts.di.IODispatcher
import com.example.mycontacts.di.MainDispatcher
import com.example.mycontacts.domain.models.User
import com.example.mycontacts.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class Login @Inject constructor(
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) :
    UseCase<User, Login.Params>(mainDispatcher, ioDispatcher) {

    private fun validateInput(params: Params): Throwable? {
        if (params.username.isEmpty()) {
            return Throwable("Username must not empty")
        }
        if (params.password.isEmpty()) {
            return Throwable("Password must not empty")
        }
        return null
    }

    override suspend fun run(params: Params): Result<User> {
        return validateInput(params)?.let(Result.Companion::failure) ?: userRepository.login(
            userName = params.username,
            password = params.password
        )
    }

    data class Params(
        val username: String,
        val password: String
    )
}