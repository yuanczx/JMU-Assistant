package com.jmu.assistant.ui.screen

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.jmu.assistant.MainActivity
import com.jmu.assistant.viewmodel.MainViewModel

@Composable
fun MainActivity.TranscriptScreen(){
    val mainViewModel by viewModels<MainViewModel>()
    Log.d("ViewModel", mainViewModel.toString())
}