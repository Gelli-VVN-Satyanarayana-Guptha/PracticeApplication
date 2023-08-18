package com.example.myandroidapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class PreferencesDataStore(context: Context){

    private val Context.dataStore by preferencesDataStore(name = "settings")
    private var pref = context.dataStore

    companion object {

    }

    suspend fun setSettings(task : String?){
        pref.edit {

        }
    }

    suspend fun getTask() = pref.data.map {

    }

}