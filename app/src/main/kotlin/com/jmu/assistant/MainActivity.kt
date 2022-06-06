package com.jmu.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jmu.assistant.ui.screen.MainScreen
import com.jmu.assistant.ui.theme.TranscriptTheme
import com.jmu.assistant.viewmodel.MainViewModel


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
