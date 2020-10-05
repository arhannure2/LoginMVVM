package com.webtech.loginapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.webtech.loginapp.data.network.AuthApi
import com.webtech.loginapp.data.network.Resource
import com.webtech.loginapp.data.repository.AuthRepository
import com.webtech.loginapp.databinding.FragmentLoginBinding
import com.webtech.loginapp.ui.*
import com.webtech.loginapp.ui.base.BaseFragment
import com.webtech.loginapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<AuthViewModel,FragmentLoginBinding,AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

      progressbar.hide()
        binding.buttonLogin.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {

            binding.progressbar.visible(it is Resource.Loading)

            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.user.access_token!!)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                    }

                }
                is Resource.Failure -> handleApiError(it)  { login() }
            }
        })

        binding.editTextTextPassword.addTextChangedListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            binding.buttonLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }


        binding.buttonLogin.setOnClickListener {

            login()


        }

    }

    private fun login()
    {
        val email = binding.editTextTextEmailAddress.text.toString().trim()
        val password = binding.editTextTextPassword.text.toString().trim()
        /// no need to show progressbar here its taken care inside Observer loading state
        //progressbar.show()
        //@todo add input validations
        viewModel.login(email, password)
    }


    override fun getViewModel(): Class<AuthViewModel> =AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding =FragmentLoginBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): AuthRepository = AuthRepository(remoteDataSource.buildApi(AuthApi::class.java),userPreferences)


}