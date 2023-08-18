package com.example.myandroidapp.ui.theme

import android.content.Context
import com.example.myandroidapp.data.User
import com.example.myandroidapp.data.UsersDatabase

class UserRepository(context: Context) {

    private val database: UsersDatabase = UsersDatabase.getDatabase(context)

    fun getUser(email: String): User? {
        return database.usersDao().getUserDetails(email)
    }

    suspend fun saveUser(user: User) {
        database.usersDao().insert(user)
    }

}