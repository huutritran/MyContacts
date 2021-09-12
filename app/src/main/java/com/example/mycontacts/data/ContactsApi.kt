package com.example.mycontacts.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ContactsApi {

    @GET("/api/users")
    suspend fun getContacts(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): ContactListResult

}