package com.example.workmanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import coil.compose.rememberAsyncImagePainter
import com.example.workmanager.ui.theme.WorkManagerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(
                constraints = Constraints(
                    requiredNetworkType = NetworkType.CONNECTED
                )
            ).build()

        val colorFilterRequest = OneTimeWorkRequestBuilder<ColorFilterWorker>()
            .setConstraints(
                constraints = Constraints(
                    requiresCharging = true
                )
            )
            .build()

        val workManager = WorkManager.getInstance(applicationContext)

        setContent {
            WorkManagerTheme {

                val workInfos = workManager
                    .getWorkInfosForUniqueWorkLiveData("download")
                    .observeAsState()
                    .value

                val downloadInfo = remember(key1 = workInfos){
                    workInfos?.find{ it.id == downloadRequest.id }
                }

                val filterInfo = remember(key1 = workInfos){
                    workInfos?.find{ it.id == colorFilterRequest.id }
                }

                val imageUri by derivedStateOf {
                    val downloadUri = downloadInfo?.outputData?.getString(WorkerKeys.IMAGE_URI)?.toUri()
                    val filterUri = filterInfo?.outputData?.getString(WorkerKeys.FILTER_URI)?.toUri()
                    filterUri ?: downloadUri
                }

                Column (
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Box (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        imageUri?.let {uri ->
                            Image(
                                painter = rememberAsyncImagePainter(model = uri),
                                contentDescription = null )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        workManager.beginUniqueWork(
                            "download",
                            ExistingWorkPolicy.KEEP,
                            downloadRequest
                        ).then(colorFilterRequest)
                            .enqueue()
                    },
                        enabled = downloadInfo?.state != WorkInfo.State.RUNNING,
                        modifier = Modifier.clip(RoundedCornerShape(10))
                    ) {
                        Text(text = "Start Download")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    when(downloadInfo?.state) {
                        WorkInfo.State.RUNNING -> Text(text = "Downloading...")
                        WorkInfo.State.SUCCEEDED -> Text(text = "Download Success")
                        WorkInfo.State.ENQUEUED -> Text(text = "Download enqueued")
                        WorkInfo.State.FAILED -> Text(text = "Download Failed")
                        WorkInfo.State.BLOCKED -> Text(text = "Download Blocked")
                        WorkInfo.State.CANCELLED -> Text(text = "Download Cancelled")
                        else -> {}
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    when(filterInfo?.state) {
                        WorkInfo.State.RUNNING -> Text(text = "Applying filter...")
                        WorkInfo.State.SUCCEEDED -> Text(text = "Filter Success")
                        WorkInfo.State.ENQUEUED -> Text(text = "Filter enqueued")
                        WorkInfo.State.FAILED -> Text(text = "Filter Failed")
                        WorkInfo.State.BLOCKED -> Text(text = "Filter Blocked")
                        WorkInfo.State.CANCELLED -> Text(text = "Filter Cancelled")
                        else -> {}
                    }
                }
            }
        }
    }
}
