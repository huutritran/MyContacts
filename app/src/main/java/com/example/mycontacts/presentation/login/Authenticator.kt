package com.example.mycontacts.presentation.login

import com.example.mycontacts.domain.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Authenticator @Inject constructor(){
    var loggedInUser: User? = null
}