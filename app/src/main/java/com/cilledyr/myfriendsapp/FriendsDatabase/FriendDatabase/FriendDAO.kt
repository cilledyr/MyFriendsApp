package com.cilledyr.myfriendsapp.FriendsDatabase.FriendDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cilledyr.myfriendsapp.BEFriend
import java.util.*

@Dao
interface FriendDAO {
    @Query("SELECT * FROM befriend WHERE id=(:id)")
    fun getFriendByID(id: UUID): LiveData<BEFriend?>

    @Query("SELECT * FROM befriend")
    fun getAllFriends(): LiveData<List<BEFriend>>

    @Update
    fun updateFriend(friend: BEFriend)

    @Insert
    fun addNewFriend(friend: BEFriend)
}