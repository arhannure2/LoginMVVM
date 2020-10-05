package com.webtech.loginapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webtech.loginapp.data.network.Resource
import com.webtech.loginapp.data.repository.AuthRepository
import com.webtech.loginapp.data.responses.LoginResponse
import com.webtech.loginapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password)
    }


    suspend fun saveAuthToken(token: String) =  repository.saveAuthToken(token)

}