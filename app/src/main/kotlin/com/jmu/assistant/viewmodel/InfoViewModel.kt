package com.jmu.assistant.viewmodel

import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jmu.assistant.utils.HttpTool
import com.jmu.assistant.utils.awaitResponse
import com.jmu.assistant.utils.buildReuest
import org.jsoup.Jsoup

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
class InfoViewModel(application: Application):BaseViewModel(application) {
    var data by mutableStateOf("")
    var loadUrl by mutableStateOf(false)
    @ExperimentalAnimationApi
    suspend fun getInfo(url:String){
        try{
        val response = HttpTool.client.newCall(buildReuest(url)).awaitResponse()
        if (response.isSuccessful){
            val document = response.body?.string()?.let { Jsoup.parse(it) }
            document?.let {
                data = it.getElementsByTag("h3")[1].parents()[0].html()
            }
        }
    }catch (e:Exception){
        loadUrl = true
    }}
}
