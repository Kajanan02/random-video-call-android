package com.plcoding.agorauikit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import android.util.Log
import androidx.compose.runtime.MutableState


class RoomViewModel: ViewModel() {

    val roomTexts: MutableState<List<String>> = mutableStateOf(emptyList())
    private val _roomName = mutableStateOf(TextFieldState())
    val roomName: State<TextFieldState> = _roomName

    private val _onJoinEvent = MutableSharedFlow<String>()
    val onJoinEvent = _onJoinEvent.asSharedFlow()
//    val roomData = roomState.rooms

    fun onRoomEnter(name: String) {
        _roomName.value = roomName.value.copy(
            text = name
        )
    }

//    data class Room(val roomName: String, val roomOwner: String)

    fun main() {
        GlobalScope.launch(Dispatchers.IO) {
//            val url = "https://mocki.io/v1/4dd85dbc-bc94-4ce2-bad9-d99d6e73e433"
//            val response = makeGetRequest(url)
//            val texts = parseResponse(response)
//            roomTexts.value = texts
//            printRoomTexts(response)
        }
    }

    private fun parseResponse(response: String): List<String> {
        val jsonArray = JSONArray(response)
        val texts = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val roomText = jsonObject.getString("room")
            texts.add(roomText)
        }
        return texts
    }
    fun printRoomTexts(response: String) {
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val roomText = jsonObject.getString("room")
            println("ROOM: $roomText")
            Log.d("Response", roomText)
        }
    }
    fun makeGetRequest(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()

            var inputLine: String?
            while (bufferedReader.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            bufferedReader.close()

            return response.toString()
        } else {
            throw Exception("Failed to make GET request. Response code: $responseCode")
        }
    }



    fun onJoinRoom() {
        println("Asdf")
//        main()
        if(roomName.value.text.isBlank()) {
            _roomName.value = roomName.value.copy(
                error = "The room can't be empty"
            )
            return
        }
        viewModelScope.launch {
            _onJoinEvent.emit(roomName.value.text)
        }
    }
}