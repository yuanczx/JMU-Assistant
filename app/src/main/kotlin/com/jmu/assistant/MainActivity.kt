package com.jmu.assistant

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import com.jmu.assistant.ui.screen.MainScreen
import com.jmu.assistant.ui.theme.TranscriptTheme
import com.jmu.assistant.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
val MainActivity.dataStore by preferencesDataStore(name = "main")

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    companion object {
        var studentID: String = ""
        var cookie: String = ""
        val COOKIE_KEY = stringPreferencesKey("cookie")
    }

    val mainViewModel by viewModels<MainViewModel>()


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.viewModelScope.launch {
            mainViewModel.judgeStartRoute(dataStore)
        }
        setContent {
            TranscriptTheme(dynamicColor = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
