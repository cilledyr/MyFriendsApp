package com.cilledyr.myfriendsapp

import androidx.lifecycle.ViewModel
import java.io.File

class FriendListViewModel : ViewModel() {

    private val friendRepo = FriendRepository.get()

    val friendListLiveData = friendRepo.getAllFriends()

    fun addFriend(friend: BEFriend) {
        friendRepo.addNewFriend(friend)
    }

    fun getPhotoFile(friend: BEFriend): File {
        return friendRepo.getPhotoFile(friend)
    }
/*val mFriends = mutableListOf<BEFriend>()

    init {

            for(i in 0 until 13) {
                val friend = BEFriend()
                friend.firstName = "John #$i"
                friend.lastName = "Doe #$i"
                friend.phoneNr = "+45 1234 567#$i"
                friend.isFavorite = i % 2 == 0
                friendRepo.addNewFriend(friend)
            }
    }*/
}