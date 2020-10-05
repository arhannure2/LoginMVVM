package com.webtech.loginapp.ui.base

import androidx.lifecycle.ViewModel
import com.webtech.loginapp.data.network.UserApi
import com.webtech.loginapp.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    suspend fun logout(api: UserApi) = withContext(Dispatchers.IO) { repository.logout(api) }

}