package com.webtech.loginapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.webtech.loginapp.data.network.AuthApi
import com.webtech.loginapp.data.network.Resource
import com.webtech.loginapp.data.repository.AuthRepository
import com.webtech.loginapp.databinding.FragmentLoginBinding
import com.webtech.loginapp.ui.base.BaseFragment
import com.webtech.loginapp.ui.enable
import com.webtech.loginapp.ui.home.HomeActivity
import com.webtech.loginapp.ui.startNewActivity
import com.webtech.loginapp.ui.visible


class LoginFragment : BaseFragment<AuthViewModel,FragmentLoginBinding,AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressbar.visible(false)
        binding.buttonLogin.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(false)
            when (it) {
                is Resource.Success -> {
                    //
                    viewModel.saveAuthToken(it.value.user.access_token!!)
                        requireActivity().startNewActivity(HomeActivity::class.java)

                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Login Failure", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.editTextTextPassword.addTextChangedListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            binding.buttonLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }


        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()
            binding.progressbar.visible(true)
            //@todo add input validations
            viewModel.login(email, password)
        }

    }


    override fun getViewModel(): Class<AuthViewModel> =AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding =FragmentLoginBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): AuthRepository = AuthRepository(remoteDataSource.buildApi(AuthApi::class.java),userPreferences)


}