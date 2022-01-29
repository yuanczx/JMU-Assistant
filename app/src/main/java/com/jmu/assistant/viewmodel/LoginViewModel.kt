package com.jmu.assistant.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmu.assistant.MainActivity.Companion.cookie
import com.jmu.assistant.MainActivity.Companion.studentID
import com.jmu.assistant.utils.TheRetrofit
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class LoginViewModel : ViewModel() {
    companion object {
        private const val BASE_URL_LOGIN =
            "https://cas.paas.jmu.edu.cn/cas/login?service=http%3A%2F%2Fjwxt.jmu.edu.cn%2Fstudent%2Fsso%2Flogin"
    }

    var url by mutableStateOf(BASE_URL_LOGIN)
}