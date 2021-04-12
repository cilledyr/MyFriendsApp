package com.cilledyr.myfriendsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.util.*
import com.cilledyr.myfriendsapp.FriendFragment.Companion as FriendFragment1

private const val TAG ="MainActivity"

class MainActivity : AppCompatActivity(), FriendListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null) {
            val fragment = FriendListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onFriendSelected(friendID: UUID) {
        val fragment = FriendFragment1.newInstance(friendID)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }
}