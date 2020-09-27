package com.webtech.loginapp.data.repository

import com.webtech.loginapp.data.UserPreferences
import com.webtech.loginapp.data.network.AuthApi

/*
Created by ​
Hannure Abdulrahim


on 9/24/2020.
 */
class AuthRepository(
    private val api: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository(){

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        api.login(email, password)
    }

    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }

}