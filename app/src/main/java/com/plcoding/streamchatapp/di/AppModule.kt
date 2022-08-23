package com.gharibe.smartapps.streamchatapp.di

import android.content.Context
import com.gharibe.smartapps.streamchatapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Provides
    @Singleton
    fun provideChatClient(@ApplicationContext context: Context) =
        ChatClient.Builder(context.getString(R.string.api_key), context).build()
}