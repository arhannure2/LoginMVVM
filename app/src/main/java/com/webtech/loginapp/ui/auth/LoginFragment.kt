package com.webtech.loginapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.webtech.loginapp.data.network.AuthApi
import com.webtech.loginapp.data.network.Resource
import com.webtech.loginapp.data.repository.AuthRepository
import com.webtech.loginapp.databinding.FragmentLoginBinding
import com.webtech.loginapp.ui.base.BaseFragment
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<AuthViewModel,FragmentLoginBinding,AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        userPreferences.saveAuthToken(it.value.user.access_token)
                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Login Failure", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()
            //@todo add input validations
            viewModel.login(email, password)
        }

    }


    override fun getViewModel(): Class<AuthViewModel> =AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding =FragmentLoginBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): AuthRepository = AuthRepository(remoteDataSource.buildApi(AuthApi::class.java))


}