package com.webtech.loginapp.data.repository

import com.webtech.loginapp.data.network.AuthApi

/*
Created by ​
Hannure Abdulrahim


on 9/24/2020.
 */
class AuthRepository(
    private val api: AuthApi
) : BaseRepository(){

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        api.login(email, password)
    }

}