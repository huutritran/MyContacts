package com.example.mycontacts.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mycontacts.domain.models.User
import com.example.mycontacts.domain.usecases.Login
import com.example.mycontacts.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: Login,
    private val authenticator: Authenticator
) : BaseViewModel() {

    val user: LiveData<User>
    get() = _user
    private val _user = MutableLiveData<User>()

    fun login(username: String, password: String) {
        val loginParams = Login.Params(
            username,
            password
        )
        loginUseCase(loginParams, viewModelScope) {
            it.fold(::onLoginSuccess, ::onFailure)
        }
    }

    private fun onLoginSuccess(user: User) {
        _user.value = user
        authenticator.loggedInUser = user
    }
}