package com.example.launchmodeexp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.launchmodeexp.ui.theme.LaunchModeExpTheme


class C : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Experiment Activity C created")
        setContent{
            LaunchModeExpTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ){
                    Text(text = "Activity C",style = MaterialTheme.typography.titleLarge)
                    Button(onClick = {
                        intent = Intent(this@C, D::class.java)
                        startActivity(intent)
                    }) {
                        Text(text = "Go to Activity D", modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        println("Experiment Activity C started")
    }

    override fun onResume() {
        super.onResume()
        println("Experiment Activity C resumed")
    }

    override fun onStop() {
        super.onStop()
        println("Experiment Activity C stopped")
    }

    override fun onPause() {
        super.onPause()
        println("Experiment Activity C paused")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Experiment Activity C destroyed")
    }

}