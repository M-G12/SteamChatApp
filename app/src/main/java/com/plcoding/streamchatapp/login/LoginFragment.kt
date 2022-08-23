package com.gharibe.smartapps.streamchatapp.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.gharibe.smartapps.streamchatapp.R
import com.gharibe.smartapps.streamchatapp.databinding.FragmentLoginBinding
import com.gharibe.smartapps.streamchatapp.ui.BindingFragment
import com.gharibe.smartapps.streamchatapp.util.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment :BindingFragment<FragmentLoginBinding>() {
   private val viewModel: LoginViewModel by viewModels()
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.setOnClickListener {
            setupConnectingUIState()
            viewModel.connectUser(binding.etUsername.text.toString())
        }
        binding.etUsername.addTextChangedListener {
            binding.etUsername.error = null
        }
        subscribeToEvent()
    }

    private fun subscribeToEvent() {
        lifecycleScope.launch {
            viewModel.logInEvent.collect{ event ->
                when(event) {
                    is LoginViewModel.LoginEvent.ErrorLogin->{
                        setupIdleUIState()
                        Toast.makeText(
                            requireContext(),
                        event.error,
                        Toast.LENGTH_LONG).show()
                    }
                    is LoginViewModel.LoginEvent.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Successful login ",
                            Toast.LENGTH_SHORT).show()
                        findNavController().navigateSafely(R.id.action_loginFragment_to_channelFragment3)

                    }
                    is LoginViewModel.LoginEvent.ErrorInputTooShort ->{
                        setupIdleUIState()
                        binding.etUsername.error = getString(R.string.error_username_too_short)
                    }
                }
            }
        }
    }

    private fun setupConnectingUIState () {
       binding.progressBar.isVisible = true
       binding.btnConfirm.isEnabled = false
    }
    private fun setupIdleUIState () {
        binding.progressBar.isVisible = false
        binding.btnConfirm.isEnabled = true
    }
}