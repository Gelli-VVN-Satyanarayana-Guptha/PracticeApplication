package com.example.todoapplication

sealed class Screens (val route: String) {
    object ToDoList : Screens("todo_list_screen")
    object AddTask : Screens("add_task_screen")
}
