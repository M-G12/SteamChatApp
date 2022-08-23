package com.gharibe.smartapps.streamchatapp.ui.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val chatClient: ChatClient
) : ViewModel(){
    private val _createChannelEvent = MutableSharedFlow<CreateChannelEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()
    fun createChannel (channelName: String) {
        val trimmerChannelName = channelName.trim()
        if (trimmerChannelName.isEmpty())return
        viewModelScope.launch {
            val result = chatClient.channel(
                "messaging",
                channelId = UUID.randomUUID().toString()
            ).create(
                mapOf(
                    "name" to trimmerChannelName
                )
            ).await()
            if (result.isError) {
                _createChannelEvent.emit(CreateChannelEvent.Error(result.error().message?:"Unknown Error"))
                return@launch
            }
            _createChannelEvent.emit(CreateChannelEvent.Success)
        }
    }
    fun logout() {
        chatClient.disconnect()
    }
    fun getUser(): User?{
        return chatClient.getCurrentUser()
    }
    sealed class CreateChannelEvent {
        data class Error (val error: String) : CreateChannelEvent()
        object Success : CreateChannelEvent()
    }
}