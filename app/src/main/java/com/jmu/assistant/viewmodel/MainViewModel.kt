package com.jmu.assistant.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class MainViewModel(application: Application): AndroidViewModel(application) {
    var selfTopBar:@Composable (()->Unit)? by mutableStateOf(null)
    var topBarLoading by mutableStateOf(false)
    fun context(): Context = getApplication<Application>().applicationContext
}