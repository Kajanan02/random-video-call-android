package com.plcoding.agorauikit

import android.Manifest
import android.util.Log
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraVideoViewer

@ExperimentalUnsignedTypes
@Composable
fun VideoScreen(
    roomName: String,
    onNavigateUp: () -> Unit = {},
    viewModel: VideoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var agoraView: AgoraVideoViewer? = null
    val navController = rememberNavController()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            viewModel.onPermissionsResult(
                acceptedAudioPermission = perms[Manifest.permission.RECORD_AUDIO] == true,
                acceptedCameraPermission = perms[Manifest.permission.CAMERA] == true,
            )
        }
    )
    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
            )
        )
    }
    BackHandler {
        agoraView?.leaveChannel()
        onNavigateUp()
    }

    fun leaveChannel(navController: NavController) {
        agoraView?.leaveChannel()
        onNavigateUp()
    }
    if(viewModel.hasAudioPermission.value && viewModel.hasCameraPermission.value) {
        AndroidView(
            factory = {
                AgoraVideoViewer(
                    it, connectionData = AgoraConnectionData(
                        appId = APP_ID,
                        appToken = TOKEN
                    )
                ).also {
                    it.join(roomName)
                    agoraView = it
                }
            },

            modifier = Modifier.fillMaxSize().padding(bottom = 40.dp)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            Button(
                onClick = {

                    leaveChannel(navController = navController)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red // Change Color.Red to any other color you desire
                )
            ) {

                Text(
                    text = "Leave Room",
                    style = MaterialTheme.typography.body1,
                    color = Color.White
                )
            }
        }
    }

}