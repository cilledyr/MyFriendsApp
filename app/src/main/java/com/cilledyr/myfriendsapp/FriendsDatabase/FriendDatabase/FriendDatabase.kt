package com.cilledyr.myfriendsapp.FriendsDatabase.FriendDatabase

import android.provider.Contacts.SettingsColumns.KEY
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cilledyr.myfriendsapp.BEFriend

@Database(entities = [BEFriend::class], version=10, exportSchema = false)
@TypeConverters(FriendTypeConverter::class)
abstract class FriendDatabase :RoomDatabase() {

    abstract fun friendDao(): FriendDAO
}

val migration_7_10 = object : Migration(7, 10) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
                "ALTER TABLE BeFriend ADD COLUMN coordinatX TEXT NOT NULL DEFAULT ''"
        )
        database.execSQL(
                "ALTER TABLE BeFriend ADD COLUMN coordinatY TEXT NOT NULL DEFAULT ''"
        )
    }
}

val migration_8_9 = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE BeFriend ADD COLUMN coordinatX TEXT NOT NULL DEFAULT ''"
        )
        database.execSQL(
            "ALTER TABLE BeFriend ADD COLUMN coordinatY TEXT NOT NULL DEFAULT ''"
        )
    }
}

val migration_9_10 = object : Migration(9, 10) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the new table
        database.execSQL(
            "CREATE TABLE friends_new " +
                    "(id TEXT NOT NULL, firstName TEXT NOT NULL, lastName TEXT NOT NULL," +
                    "phoneNr TEXT NOT NULL, website TEXT NOT NULL, birthday INTEGER NOT NULL, email TEXT NOT NULL "+
                    "coordinatX TEXT NOT NULL, coordinatY TEXT NOT NULL, isFavorite INTEGER NOT NULL " +
                    "PRIMARY KEY(id))");
        // Copy the data
        database.execSQL(
            "INSERT INTO friends_new (Id, firstName, lastName,phoneNr, website, birthday, email, coordinatX, coordinatY, isFavorite) SELECT " +
                    "Id, firstName, lastName, phoneNr, website, birthday, email, coordinatX, coordinatY, isFavorite FROM BeFriend");
                    // Remove the old table
                    database.execSQL("DROP TABLE BeFriend");
        // Change the table name to the correct one
        database.execSQL("ALTER TABLE friends_new RENAME TO BeFriend");
    }
}
