package com.example.todoapplication.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel (private val dao: TaskDao ) : ViewModel() {

    private val _tasks = dao.getTasks()
    private val _state = MutableStateFlow(TaskState())

    val state = combine(_state, _tasks) { state,tasks ->
        state.copy(
            taskList = tasks,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    fun onEvent(event: TaskEvent) {
        when(event) {

            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    dao.deleteTask(event.task)
                }
            }

            is TaskEvent.UpdateTask -> {
                viewModelScope.launch{
                    dao.upsertTask(event.task)
                }
                _state.update {
                    it.copy(
                        id = 0,
                        taskDesc = "",
                        desc = "",
                        isUpdatingTask = false
                    )
                }
            }

            TaskEvent.AddTask -> {
                val task = state.value.taskDesc
                val desc = state.value.desc

                if (task.isBlank()) {
                    return
                }

                val temp = Task(
                    task = task,
                    desc = desc
                )
                viewModelScope.launch {
                    dao.upsertTask(temp)
                }
                _state.update {
                    it.copy(
                        taskDesc = "",
                        desc = ""
                    )
                }
            }

            is TaskEvent.ModifyDetails -> {
                _state.update {
                    it.copy(
                        id = event.id,
                        taskDesc = event.task,
                        desc = event.desc,
                        isUpdatingTask = event.isUpdate
                    )
                }
            }

        }
    }
}