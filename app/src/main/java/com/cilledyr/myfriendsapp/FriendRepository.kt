package com.cilledyr.myfriendsapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.cilledyr.myfriendsapp.FriendsDatabase.FriendDatabase.FriendDatabase
import com.cilledyr.myfriendsapp.FriendsDatabase.FriendDatabase.migration_3_6
import com.cilledyr.myfriendsapp.FriendsDatabase.FriendDatabase.migration_8_9
import java.io.File
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "friend-database"

class FriendRepository private constructor(context: Context) {

    private val database: FriendDatabase = Room.databaseBuilder(
        context.applicationContext,
        FriendDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(migration_8_9)
        .build()

    private val friendDao = database.friendDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getAllFriends(): LiveData<List<BEFriend>> = friendDao.getAllFriends()

    fun getFriendByID(id: UUID): LiveData<BEFriend?> = friendDao.getFriendByID(id)

    fun getFriendByIDString(id: String): LiveData<BEFriend?> = friendDao.getFriendByIDString(id)

    fun updateFriend(friend: BEFriend) {
        executor.execute {
            friendDao.updateFriend(friend)
        }
    }

    fun addNewFriend(friend: BEFriend) {
        executor.execute {
            friendDao.addNewFriend(friend)
        }
    }

    fun deleteFriend(friend: BEFriend) {
        executor.execute {
            friendDao.deleteFriend(friend)
        }
    }

    fun getPhotoFile(friend: BEFriend): File = File(filesDir, friend.photoFileName)

    companion object{
        private var INSTANCE: FriendRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = FriendRepository(context)
            }
        }

        fun get(): FriendRepository {
            return INSTANCE ?:
            throw IllegalStateException("Crime Repository must be initialised")
        }
    }
}