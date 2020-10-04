package com.webtech.loginapp.data.responses

import com.webtech.loginapp.data.network.UserApi
import com.webtech.loginapp.data.repository.BaseRepository


class UserRepository(
    private val api: UserApi
) : BaseRepository() {

    suspend fun getUser() = safeApiCall {
        api.getUser()
    }

}