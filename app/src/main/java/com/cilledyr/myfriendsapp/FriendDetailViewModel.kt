package com.cilledyr.myfriendsapp

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class FriendDetailViewModel() : ViewModel() {
    private val friendRepo = FriendRepository.get()
    private val friendIdLiveData = MutableLiveData<String>()

    var friendLiveData: LiveData<BEFriend?> =
        Transformations.switchMap(friendIdLiveData) { friendId ->
            friendRepo.getFriendByIDString(friendId)
        }

    fun loadFriend(friendId: String) {
        friendIdLiveData.value = friendId
    }

    fun saveFriend(friend: BEFriend) {
        friendRepo.updateFriend(friend)
    }

    fun getPhotoFile(friend: BEFriend): File {
        return friendRepo.getPhotoFile(friend)
    }


    fun deleteFriend(friend: BEFriend) {
        friendRepo.deleteFriend(friend) //Send through repo to delete the friend.
    }
}