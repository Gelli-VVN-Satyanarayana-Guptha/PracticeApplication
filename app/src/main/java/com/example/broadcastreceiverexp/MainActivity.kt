package com.example.broadcastreceiverexp

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.broadcastreceiverexp.ui.theme.BroadcastReceiverExpTheme

class MainActivity : ComponentActivity() {

    private val airplaneModeReceiver = AirplaneModeReceiver()

    //This was the the receiver of my broadcast
    private val myBroadcastReceiver = MyBroadcastReceiver()

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically registering the broadcast receivers
        // Which means it receives broadcast only when the activity was running
        // Static receivers receive broadcast even if the application was not running

        registerReceiver(
            airplaneModeReceiver,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )

        //Registering my broadcast receiver
        registerReceiver(
            myBroadcastReceiver,
            IntentFilter("TEST_ACTION")
        )

        setContent {
            BroadcastReceiverExpTheme {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Button(onClick = {
                        // sending the broadcast
                        sendBroadcast(
                            Intent("TEST_ACTION")
                        )
                    }) {
                        Text(text = "Broadcast", modifier = Modifier.padding(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    YoutubeIntent(context = this@MainActivity)

                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // unregistering the broadcast receivers to avoid memory leak
        unregisterReceiver(airplaneModeReceiver)
        unregisterReceiver(myBroadcastReceiver)

    }
}

@Composable
fun YoutubeIntent(context : Context){
    Button(onClick = {
        // Declaring an intent to open youtube
        Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/2hIY1xuImuQ?si=J1hFkeimoHwvvAT4")).also{
            // it.`package` = "com.google.android.youtube"
            try {
                ContextCompat.startActivity(context, it, Bundle())
            } catch (e : ActivityNotFoundException){
                println("Activity Not Found")
            }
        }
    }) {
        Text(text = "Open Youtube", modifier = Modifier.padding(8.dp))
    }
}
