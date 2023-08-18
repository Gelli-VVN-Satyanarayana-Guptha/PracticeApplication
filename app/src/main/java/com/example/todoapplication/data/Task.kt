package com.example.todoapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    var task : String,
    var desc : String
)
