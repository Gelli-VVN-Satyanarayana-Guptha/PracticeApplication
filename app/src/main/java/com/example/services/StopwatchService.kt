package com.example.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject

import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class StopwatchService : Service() {

    @Inject
    lateinit var notificationManager : NotificationManager

    private var notificationBuilder = NotificationCompat.Builder(this, "stopwatch_notifications")
        .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
        .setContentTitle("Stopwatch")
        .setContentText("00:00:00")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setOngoing(true)

    private val binder = StopwatchBinder()

    @RequiresApi(Build.VERSION_CODES.O)
    private var duration: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(StopwatchState.Idle)
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("onStart","stopwatch")
        when (intent?.action) {
            StopwatchState.Start.toString() -> {
                startForegroundService()
                startStopwatch { hours, minutes, seconds ->
                    updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                }
            }
            StopwatchState.Stop.toString() -> {
                stopStopwatch()
                stopForegroundService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? = binder

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startStopwatch(onTick: (h: String, m: String, s: String) -> Unit) {
        currentState.value = StopwatchState.Start
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun stopStopwatch() {

        if (this::timer.isInitialized) {
            timer.cancel()
        }

        duration = Duration.ZERO
        currentState.value = StopwatchState.Idle
        updateTimeUnits()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@StopwatchService.hours.value = hours.toInt().pad()
            this@StopwatchService.minutes.value = minutes.pad()
            this@StopwatchService.seconds.value = seconds.pad()
        }
    }

    private fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

    private fun startForegroundService() {

        createNotificationChannel()

        val notification = notificationBuilder.build()

        startForeground(1, notification)
    }

    private fun stopForegroundService() {
        notificationManager.cancel(1)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "stopwatch_notifications",
                "Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            1,
            notificationBuilder.setContentText(
                "$hours:$minutes:$seconds"
            ).build()
        )
    }

    inner class StopwatchBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }

}

enum class StopwatchState {
    Idle,
    Start,
    Stop
}