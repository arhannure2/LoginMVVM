package com.webtech.loginapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.webtech.loginapp.R
import com.webtech.loginapp.data.network.Resource
import com.webtech.loginapp.data.network.UserApi
import com.webtech.loginapp.data.responses.User
import com.webtech.loginapp.data.responses.UserRepository
import com.webtech.loginapp.databinding.FragmentHomeBinding

import com.webtech.loginapp.ui.base.BaseFragment
import com.webtech.loginapp.ui.hide
import com.webtech.loginapp.ui.show
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding,UserRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressbar.hide()

        viewModel.getUser()

        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    progressbar.hide()
                    updateUI(it.value.user)
                }
                is Resource.Loading -> {
                    progressbar.show()
                }
            }
        })

        binding.buttonLogout.setOnClickListener {
            //logout()
        }
    }

    private fun updateUI(user: User) {
        with(binding) {
            textViewId.text = user.id.toString()
            textViewName.text = user.name
            textViewEmail.text = user.email
        }
    }
    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): UserRepository {

        //// userPreferences.authToken.first() here first() is suspend fun which should be call only inside suspend fun or coroutine scope
        // suspend funs are  async call but here we need sync call so we can not call inside suspend fun
        /// but issues is if we call here app will go in ANR ( Application Not Responding
        /// another soutuion is call token when fragment is loaded so check BaseFragment
        //it should be call inside onCreateView-> lifecycleScope.launch { }  ( check BaseFragment)
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(UserApi::class.java, token)
        return UserRepository(api)
    }


}