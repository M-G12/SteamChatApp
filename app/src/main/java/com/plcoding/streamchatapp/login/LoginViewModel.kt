package com.gharibe.smartapps.streamchatapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gharibe.smartapps.streamchatapp.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var client: ChatClient
) : ViewModel() {
    private val _logInEvent = MutableSharedFlow<LoginEvent>()
    val logInEvent = _logInEvent.asSharedFlow()

    private fun isValidUsername(username: String) =
    username.length >= Constant.MIN_USERNAME_LENGTH

    fun connectUser(username: String) {
        val trimmerUsername= username.trim()
        viewModelScope.launch {
            if (isValidUsername(trimmerUsername)){
                val result = client.connectGuestUser(
                    userId = trimmerUsername,
                    username = trimmerUsername
                ).await()
                if (result.isError){
                    _logInEvent.emit(LoginEvent.ErrorLogin(result.error().message ?: "Unknown message"))
                    return@launch
                }
                _logInEvent.emit(LoginEvent.Success)
            } else {
                _logInEvent.emit(LoginEvent.ErrorInputTooShort)
            }
        }
    }
    sealed class LoginEvent {
        object ErrorInputTooShort : LoginEvent()
        data class ErrorLogin(val error: String) : LoginEvent()
        object Success : LoginEvent()
    }
}