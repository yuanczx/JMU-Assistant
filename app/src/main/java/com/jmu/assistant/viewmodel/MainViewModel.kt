package com.jmu.assistant.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.jmu.assistant.MainActivity
import com.jmu.assistant.utils.TheRetrofit
import org.jsoup.Jsoup
import retrofit2.awaitResponse

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var loadFinish by mutableStateOf(false)
    var rightData by mutableStateOf("")
    var showInfo by mutableStateOf(false)
    var actions: @Composable (RowScope.() -> Unit) by mutableStateOf({})
    var floaAction: @Composable () -> Unit by mutableStateOf({})
    var navigationIcon: @Composable () -> Unit by mutableStateOf({})
    var image by mutableStateOf("http://jwxt.jmu.edu.cn")
    var title by mutableStateOf("JMU Assistant")
    var basicInfo by mutableStateOf("")
    fun getImageRequest() = ImageRequest.Builder(context())
        .data(image)
        .setHeader("Cookie", MainActivity.cookie)
        .transformations(RoundedCornersTransformation(topLeft = 25f, bottomLeft = 25f))
        .build()

    fun context(): Context = getApplication<Application>().applicationContext

    suspend fun getStudentInfo() {
        try {
            val response = TheRetrofit.api.getStudentInfo(MainActivity.studentID).awaitResponse()
            val jsoup = Jsoup.parse(response.body().toString())
            val name = jsoup.getElementsByClass("studentName")[0].html()
            rightData +=
                jsoup.getElementsByClass("base-header-right")[0].text()
                    .replace(Regex("(?=([男女学]|(证件号)))|((证件类型).*?(?=\\s) )"), "\n")
            image = "http://jwxt.jmu.edu.cn" + jsoup.getElementsByTag("img")[0].attr("src")
            val tds = jsoup.getElementsByClass("table-three-bisection")[0].getElementsByTag("td")
            repeat(tds.size / 2) {
                basicInfo += "${tds[it * 2].html()} : ${tds[it * 2 + 1].html()}\n"
            }
            showInfo = true
            Log.d("html", image)
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }


}