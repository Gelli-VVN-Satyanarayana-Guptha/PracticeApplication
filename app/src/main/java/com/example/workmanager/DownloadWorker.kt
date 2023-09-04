package com.example.workmanager

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanager.WorkerKeys.ERROR_MSG
import com.example.workmanager.WorkerKeys.IMAGE_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

class DownloadWorker(
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        showNotification()

        delay(30000L)

        val response = FileApi.instance.downloadImage()

        response.body()?.let { body ->
            return withContext(Dispatchers.IO){
                val file = File(applicationContext.cacheDir,"image.jpg")
                val outputStream = FileOutputStream(file)

                outputStream.use { stream ->
                    try {
                        stream.write(body.bytes())
                    } catch (e : IOException){
                        return@withContext Result.failure(
                            workDataOf(
                                ERROR_MSG to e.localizedMessage
                            )
                        )
                    }
                }

                Result.success(
                    workDataOf(
                        IMAGE_URI to file.toUri().toString()
                    )
                )
            }
        }

        if(!response.isSuccessful){
            if (response.code().toString().startsWith("5")){
                return Result.retry()
            }
            return Result.failure(
                workDataOf(
                    ERROR_MSG to "Network Error"
                )
            )
        }

        return Result.failure(
            workDataOf(
                ERROR_MSG to "Unknown Error"
            )
        )
    }


    private suspend fun showNotification( ){
        val notification = NotificationCompat.Builder(applicationContext, "download_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Downloading...")
            .setContentText("in progress")
            .build()

        setForeground(
            ForegroundInfo(
                Random.nextInt(), notification
            )
        )
    }
}