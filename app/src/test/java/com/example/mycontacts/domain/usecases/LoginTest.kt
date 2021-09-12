package com.example.mycontacts.domain.usecases

import com.example.mycontacts.domain.models.User
import com.example.mycontacts.domain.repositories.UserRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test

class LoginTest : BaseTest() {
    private lateinit var loginUseCase: Login
    private lateinit var userRepository: UserRepository

    override fun setup() {
        super.setup()
        userRepository = mockk(relaxed = true)
        loginUseCase = Login(
            mainDispatcher,
            ioDispatcher,
            userRepository
        )
    }

    @Test
    fun `login should return error when username empty`() {
        //given
        val params = Login.Params(
            username = "",
            password = "password"
        )
        var result: Result<User>? = null

        //when
        loginUseCase(params, scope) { result = it }

        //then
        result shouldNotBe null
        result!!.isFailure shouldBe true
    }

    @Test
    fun `login should return error when password empty`() {
        //given
        val params = Login.Params(
            username = "username",
            password = ""
        )
        var result: Result<User>? = null

        //when
        loginUseCase(params, scope) { result = it }

        //then
        result shouldNotBe null
        result!!.isFailure shouldBe true
    }

    @Test
    fun `login should return error when given invalid data`() {
        //given
        val params = Login.Params(
            username = "username",
            password = "password"
        )
        var result: Result<User>? = null
        coEvery { userRepository.login(any(), any()) } returns Result.failure(error)

        //when
        loginUseCase(params, scope) { result = it }

        //then
        result shouldNotBe null
        result!!.isFailure shouldBe true
        result!!.exceptionOrNull() shouldBe error
        coVerify { userRepository.login(password = params.password, userName = params.username) }
    }

    @Test
    fun `login should return user when success`() {
        //given
        val params = Login.Params(
            username = "username",
            password = "password"
        )
        var result: Result<User>? = null
        coEvery { userRepository.login(any(), any()) } returns Result.success(user)

        //when
        loginUseCase(params, scope) { result = it }

        //then
        result shouldNotBe null
        result!!.isSuccess shouldBe true
        result!!.getOrNull() shouldBe user
        coVerify { userRepository.login(password = params.password, userName = params.username) }
    }

    private companion object {
        val error = Error("error message")
        val user = User(
            name = "name",
            email = "email@mail.com"
        )
    }
}