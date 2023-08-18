package com.example.todoapplication.data

import kotlinx.coroutines.flow.Flow

class OfflineTasksRepository (private val taskDao: TaskDao) : TasksRepository {

    override suspend fun insertTask(task: Task) {
        taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.upsertTask(task)
    }

    override fun getTasksStream(): Flow<List<Task>> {
        return taskDao.getTasks()
    }

}