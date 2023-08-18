package com.example.myandroidapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    companion object {
        @Volatile
        private var Instance: UsersDatabase? = null

        fun getDatabase(context: Context): UsersDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, UsersDatabase::class.java, "users_database")
                    .build()
                    .also { Instance = it }
            }
        }

    }
}