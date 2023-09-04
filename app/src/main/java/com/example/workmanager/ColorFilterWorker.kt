package com.example.workmanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.LightingColorFilter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ColorFilterWorker (
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val imageFile = params.inputData.getString(WorkerKeys.IMAGE_URI)?.toUri()?.toFile()
        delay(5000L)
        return imageFile?.let {file ->
            val bmp = BitmapFactory.decodeFile(file.absolutePath)
            val resultBitmap = bmp.copy(bmp.config,true)
            val paint = Paint()
            paint.colorFilter = ColorFilter.lighting(Color(0x08FF04), Color(1))
            val canvas = Canvas(resultBitmap.asImageBitmap())
            canvas.drawImage(resultBitmap.asImageBitmap(), Offset.Zero,paint)

            withContext(Dispatchers.IO){
                val resultImageFile = File(appContext.cacheDir,"filter_img.jpg")
                val outputStream = FileOutputStream(resultImageFile)
                val successful = resultBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    outputStream
                )
                if(successful){
                    Result.success(
                        workDataOf(
                            WorkerKeys.FILTER_URI to resultImageFile.toUri().toString()
                        )
                    )
                }else Result.failure()
            }
        } ?: Result.failure()
    }
}

