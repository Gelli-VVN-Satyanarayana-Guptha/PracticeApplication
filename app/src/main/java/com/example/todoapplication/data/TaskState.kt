package com.example.todoapplication.data

data class TaskState(
    val taskList: List<Task> = emptyList(),
    var id : Int = 0,
    var taskDesc: String = "",
    var desc: String = "",
    var isUpdatingTask: Boolean = false
)
