package com.webtech.loginapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.webtech.loginapp.data.UserPreferences
import com.webtech.loginapp.data.network.RemoteDataSource
import com.webtech.loginapp.data.network.UserApi
import com.webtech.loginapp.data.repository.BaseRepository
import com.webtech.loginapp.ui.auth.AuthActivity
import com.webtech.loginapp.ui.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/*
Created by ​
Hannure Abdulrahim


on 9/24/2020.
 */
abstract class BaseFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {

    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        lifecycleScope.launch { userPreferences.authToken.first() }
        return binding.root
    }


    fun logout() = lifecycleScope.launch{
        val authToken = userPreferences.authToken.first()

        val api = remoteDataSource.buildApi(UserApi::class.java, authToken)
        viewModel.logout(api)
        userPreferences.clear()
        requireActivity().startNewActivity(AuthActivity::class.java)
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R

}