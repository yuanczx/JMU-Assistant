package com.yuan.transcript.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.yuan.transcript.models.Activity
import com.yuan.transcript.models.CourseTable
import okhttp3.*
import java.io.IOException
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class HomeViewModel : ViewModel() {
    var title by mutableStateOf("")
    var btmNavigationItem by mutableStateOf(0)
}
