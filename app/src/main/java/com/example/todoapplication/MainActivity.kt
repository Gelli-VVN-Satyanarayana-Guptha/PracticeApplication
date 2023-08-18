package com.example.todoapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.todoapplication.data.Task
import com.example.todoapplication.data.TaskDatabase
import com.example.todoapplication.data.TaskEvent
import com.example.todoapplication.data.TaskState
import com.example.todoapplication.data.TaskViewModel
import com.example.todoapplication.ui.theme.ToDoApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db by lazy {
            Room.databaseBuilder(
                applicationContext,
                TaskDatabase::class.java,
                "tasks.db"
            ).build()
        }
        val viewModel by viewModels<TaskViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return TaskViewModel(db.dao) as T
                    }
                }
            }
        )

        setContent {
            ToDoApplicationTheme {
                val state by viewModel.state.collectAsState()
                Navigation(state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}


@Composable
fun Navigation(state: TaskState, onEvent: KFunction1<TaskEvent, Unit>) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.ToDoList.route
    ) {
        composable(route = Screens.ToDoList.route){
            ToDoList(state,onEvent, navController = navController)
        }
        composable(route = Screens.AddTask.route){
            AddTask(state,onEvent,navController = navController)
        }
    }
}

@Composable
fun ToDoList(state: TaskState, onEvent: KFunction1<TaskEvent, Unit>, navController: NavController) {

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(vertical = 0.5f.dp)
                .background(Color.Black),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tasks to Complete",
                color = Color.White,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
        }

        LazyColumn {
            items(state.taskList.size) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, start = 6.dp, end = 6.dp)
                        .background(color = Color.Cyan)
                        .clickable {
                            state.taskDesc = state.taskList[it].task
                            state.desc = state.taskList[it].desc
                            state.id = state.taskList[it].id
                            state.isUpdatingTask = true
                            navController.navigate(Screens.AddTask.route)
                         },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column ()
                    {
                        Text(
                            text = state.taskList[it].task,
                            modifier = Modifier
                                .padding(start = 15.dp, top = 8.dp, bottom = 4.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = state.taskList[it].desc,
                            modifier = Modifier
                                .padding(start = 15.dp, bottom = 8.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 20.dp)
                            .clickable {
                                onEvent(TaskEvent.DeleteTask(state.taskList[it]))
                            }
                    )
                }
                
            }
        }

        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddTask.route)
                },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myTextField(field : String) : String {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.padding(bottom = 20.dp),
        value = text,
        onValueChange = { text = it },
        label = { Text(field) }
    )
    return text
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTask(state: TaskState, onEvent: KFunction1<TaskEvent, Unit>,navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                "Add Task",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            OutlinedTextField(
                modifier = Modifier.padding(bottom = 20.dp),
                value = state.taskDesc,
                onValueChange = {
                    onEvent(TaskEvent.ModifyDetails(state.id,it,state.desc,state.isUpdatingTask))
                },
                label = { "Task" }
            )

            OutlinedTextField(
                modifier = Modifier.padding(bottom = 20.dp),
                value = state.desc,
                onValueChange = {
                    onEvent(TaskEvent.ModifyDetails(state.id,state.taskDesc,it,state.isUpdatingTask))
                },
                label = { "Description" }
            )

            Button(
                modifier = Modifier.padding(top = 40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                ),
                shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 10)),
                onClick = {
                    if(state.isUpdatingTask){
                        val temp = Task(state.id,state.taskDesc,state.desc)
                        onEvent(TaskEvent.UpdateTask(temp))
                        navController.navigate(Screens.ToDoList.route)
                    }else{
                        onEvent(TaskEvent.AddTask)
                        navController.navigate(Screens.ToDoList.route)
                    }
                },
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = if(state.isUpdatingTask) "Update" else "Schedule",
                        color = Color.White)
                }
            }

        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun ToDoPreview() {
    ToDoApplicationTheme {

    }
}
 */