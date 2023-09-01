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

class D : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Experiment Activity D created")
        setContent{
            LaunchModeExpTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ){
                    Text(text = "Activity D",style = MaterialTheme.typography.titleLarge)
                    Button(onClick = {
                        intent = Intent(this@D, A::class.java)
                        startActivity(intent)
                    }) {
                        Text(text = "Go to Activity A", modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        println("Experiment Activity D started")
    }

    override fun onResume() {
        super.onResume()
        println("Experiment Activity D resumed")
    }

    override fun onStop() {
        super.onStop()
        println("Experiment Activity D stopped")
    }

    override fun onPause() {
        super.onPause()
        println("Experiment Activity D paused")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Experiment Activity D destroyed")
    }
}