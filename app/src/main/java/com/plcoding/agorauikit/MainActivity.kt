package com.plcoding.agorauikit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plcoding.agorauikit.ui.theme.AgoraUIKitTheme

const val APP_ID = "8f44d24766f445d69905621ffb5114ab"
const val TOKEN = "007eJxTYPjX+a7xX9r/jA1neTKaZ/Yemql1KrCr6GrSFhk2m437eLYrMFikmZikGJmYm5kBGaYpZpaWBqZmRoZpaUmmhoYmiUnqMp9TGwIZGYImM7MyMkAgiM/CUJKaW8DAAADNUB/Z"
//const val channelName = "temp"

@ExperimentalUnsignedTypes
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgoraUIKitTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.padding(16.dp)
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "room_screen"
                    ) {
                        composable(route = "room_screen") {
                            RoomScreen(onNavigate = navController::navigate)
                        }
                        composable(
                            route = "video_screen/{roomName}",
                            arguments = listOf(
                                navArgument(name = "roomName") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val roomName = it.arguments?.getString("roomName") ?: return@composable
                            VideoScreen(
                                roomName = roomName,
                                onNavigateUp = navController::navigateUp
                            )
                        }
                    }
                }
            }
        }
    }
}