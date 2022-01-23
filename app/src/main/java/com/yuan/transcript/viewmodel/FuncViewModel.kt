package com.yuan.transcript.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.yuan.transcript.models.CourseTable


class FuncViewModel:ViewModel() {


    var requirePermission by mutableStateOf(false)
    var loadCourse by mutableStateOf(false)
    var courseTable: CourseTable? by mutableStateOf(null)
    var login by mutableStateOf(false)

    var cookie by mutableStateOf("")

    private var studentId = 12267
    private var semester = 181

    val gson = Gson()

    var showAlert by mutableStateOf(false)






}