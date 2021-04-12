package com.cilledyr.myfriendsapp

import android.app.Application

class FriendIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FriendRepository.Companion.initialize(this)
    }
}