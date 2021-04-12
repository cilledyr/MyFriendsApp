package com.cilledyr.myfriendsapp.FriendsDatabase.FriendDatabase

import android.location.Address
import androidx.room.TypeConverter
import java.util.*

private const val SEPERATOR = "&"
class FriendTypeConverter {

    @TypeConverter
    fun FromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun fromAddress(address: Address?) : String? {
        val length = address?.maxAddressLineIndex
        if(length != -1) {
            val theAddress = mutableListOf<String>()
            var i = 0
            while (i <= length!!) {
                theAddress += address.getAddressLine(i)
                i++
            }
            return theAddress.map {
                it
            }.joinToString (separator = SEPERATOR)
        }
        else
        {
            return address.toString()
        }

    }

    @TypeConverter
    fun toAddress(theAddress: String?) : Address {
        var address = Address(Locale.getDefault())
        var theNewAddress = theAddress?.split(SEPERATOR)?.map { it }?.toMutableList()
        var i = 0
        while( i <= theNewAddress?.size!!) {
            address.setAddressLine(i, theNewAddress[i])
        }
        return address
    }
}