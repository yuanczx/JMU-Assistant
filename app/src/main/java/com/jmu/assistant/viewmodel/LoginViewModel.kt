package com.jmu.assistant.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmu.assistant.MainActivity
import com.jmu.assistant.utils.TheRetrofit
import kotlinx.coroutines.launch
import okhttp3.*
import retrofit2.awaitResponse
import java.io.IOException
import java.util.concurrent.TimeUnit

class LoginViewModel : ViewModel() {
    companion object {
        const val BASE_URL_LOGIN =
            "https://cas.paas.jmu.edu.cn/cas/login?service=http%3A%2F%2Fjwxt.jmu.edu.cn%2Fstudent%2Fsso%2Flogin"
    }

    var login by mutableStateOf(false)
    var url by mutableStateOf(BASE_URL_LOGIN)

    fun getStudentID() {
        viewModelScope.launch {
            val redirect = TheRetrofit.api.getStudentId().awaitResponse().headers()["Location"]
            MainActivity.studentID = redirect?.substringAfter("/info/").toString()
            Log.d("ID", MainActivity.studentID)
            if (MainActivity.studentID.isNotBlank() && MainActivity.cookie.isNotBlank()) login =
                true
        }
    }
}