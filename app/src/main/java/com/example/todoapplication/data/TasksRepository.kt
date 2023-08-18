package com.example.todoapplication.data

import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    fun getTasksStream(): Flow<List<Task>>

}