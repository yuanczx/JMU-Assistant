package com.jmu.assistant.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jmu.assistant.MainActivity
import com.jmu.assistant.utils.TheRetrofit
import org.jsoup.Jsoup
import retrofit2.awaitResponse

class UserViewModel: ViewModel() {
    suspend fun getStudentInfo() {
        val response = TheRetrofit.api.getStudentInfo(MainActivity.studentID).awaitResponse()
        if (response.isSuccessful){
            val jsoup = Jsoup.parse(response.body().toString())
            val a =jsoup.body().getElementsByClass("table table-three-bisection")[0]
            Log.d("jsoup",a.html())
        }
    }
}