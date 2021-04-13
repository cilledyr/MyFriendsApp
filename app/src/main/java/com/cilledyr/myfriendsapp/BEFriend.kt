package com.cilledyr.myfriendsapp

import android.location.Address
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class BEFriend(@PrimaryKey val id: UUID = UUID.randomUUID(),
                    var firstName: String = "",
                    var lastName: String = "",
                    var phoneNr: String = "",
                    var website: String = "",
                    var birthday: Date = Date(),
                    var email: String = "",
                    //var address: Address  = Address(Locale.getDefault()),
                    var isFavorite: Boolean = false) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
