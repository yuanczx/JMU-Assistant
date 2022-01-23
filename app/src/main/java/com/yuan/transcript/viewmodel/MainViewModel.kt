package com.yuan.transcript.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    companion object{
        const val BASE_URL_LOGIN =
            "https://cas.paas.jmu.edu.cn/cas/login?service=http%3A%2F%2Fjwxt.jmu.edu.cn%2Fstudent%2Fsso%2Flogin"
    }
    var login by mutableStateOf(false)
    var url by mutableStateOf(BASE_URL_LOGIN)
    var cookie by mutableStateOf("")
}