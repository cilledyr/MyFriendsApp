package com.cilledyr.myfriendsapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class BEFriend(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                    var firstName: String = "",
                    var lastName: String = "",
                    var phoneNr: String = "",
                    var website: String = "",
                    var birthday: Date = Date(),
                    var email: String = "",
                    var coordinatX: String = "",
                    var coordinatY: String = "",
                    //var coordinateX: Double = 0.0,
                    //var coordinateY: Double = 0.0,
                    //var address: Address  = Address(Locale.getDefault()),
                    var isFavorite: Boolean = false) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
