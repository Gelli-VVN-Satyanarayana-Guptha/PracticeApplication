package com.example.todoapplication.data

sealed interface TaskEvent {
    data class ModifyDetails(val id : Int,val task: String,val desc: String, val isUpdate : Boolean) : TaskEvent
    object AddTask: TaskEvent
    data class UpdateTask(val task: Task): TaskEvent
    data class DeleteTask(val task: Task): TaskEvent
}