package com.cilledyr.myfriendsapp.FriendsDatabase.FriendDatabase

import android.provider.Contacts.SettingsColumns.KEY
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cilledyr.myfriendsapp.BEFriend

@Database(entities = [BEFriend::class], version=8, exportSchema = false)
@TypeConverters(FriendTypeConverter::class)
abstract class FriendDatabase :RoomDatabase() {

    abstract fun friendDao(): FriendDAO
}

val migration_7_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE BeFriend ADD COLUMN coordinateX TEXT NOT NULL DEFAULT ''"
        )
    }
}

val migration_3_6 = object : Migration(3, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the new table
        database.execSQL(
            "CREATE TABLE friends_new " +
                    "(id TEXT NOT NULL, firstName TEXT NOT NULL, lastName TEXT NOT NULL, email TEXT NOT NULL, " +
                    "phoneNr TEXT NOT NULL, birthday INTEGER NOT NULL, isFavorite INTEGER NOT NULL,coordinateX DOUBLE NOT NULL, coordinateY DOUBLE NOT NULL," +
                    "PRIMARY KEY(id))");
        // Copy the data
        database.execSQL(
            "INSERT INTO friends_new (Id, firstName, lastName, email, phoneNr, birthday, isFavorite) SELECT " +
                    "Id, firstName, lastName, email, phoneNr, birthday, isFavorite, coordinateX, coordinateY FROM BeFriend");
                    // Remove the old table
                    database.execSQL("DROP TABLE BeFriend");
        // Change the table name to the correct one
        database.execSQL("ALTER TABLE friends_new RENAME TO BeFriend");
    }
}
