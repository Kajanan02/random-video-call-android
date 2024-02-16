package com.plcoding.agorauikit

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.*

@Composable
fun RoomScreen(
    onNavigate: (String) -> Unit,
    viewModel: RoomViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val roomTexts by viewModel.roomTexts
    LaunchedEffect(key1 = true) {
        viewModel.main()
        viewModel.onJoinEvent.collectLatest { name ->
            onNavigate("video_screen/$name")
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Join a room or create a new one",
            style = MaterialTheme.typography.h4
        )
        roomTexts.forEach { roomText ->
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    viewModel.onRoomEnter(roomText)
                    viewModel.onJoinRoom()
                }
            ) {

                Text(
                    text = "ROOM: $roomText",
                    style = MaterialTheme.typography.body1
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = viewModel.roomName.value.text,
            onValueChange = viewModel::onRoomEnter,
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.roomName.value.error != null,
            placeholder = {
                Text(text = "Enter a room name")
            }
        )
        viewModel.roomName.value.error?.let {
            Text(text = it, color = MaterialTheme.colors.error)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = viewModel::onJoinRoom) {
            Text(text = "Join")
        }
    }
}