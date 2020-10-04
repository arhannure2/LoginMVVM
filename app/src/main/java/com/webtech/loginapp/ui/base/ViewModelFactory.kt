package com.webtech.loginapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.webtech.loginapp.data.repository.AuthRepository
import com.webtech.loginapp.data.repository.BaseRepository
import com.webtech.loginapp.data.responses.UserRepository
import com.webtech.loginapp.ui.auth.AuthViewModel
import com.webtech.loginapp.ui.home.HomeViewModel

import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as UserRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }

}