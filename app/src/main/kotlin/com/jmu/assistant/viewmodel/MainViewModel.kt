package com.jmu.assistant.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.AndroidViewModel
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.jmu.assistant.MainActivity
import com.jmu.assistant.MainActivity.Companion.COOKIE_KEY
import com.jmu.assistant.MainActivity.Companion.studentID
import com.jmu.assistant.entity.BtmNav
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.utils.TheRetrofit
import kotlinx.coroutines.flow.first
import org.jsoup.Jsoup
import retrofit2.awaitResponse

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class MainViewModel(application: Application) : AndroidViewModel(application) {

    var showTopBar by mutableStateOf(true)
    var rightData by mutableStateOf("")
    var showInfo by mutableStateOf(false)
    var actions: @Composable (RowScope.() -> Unit) by mutableStateOf({})
    var floatAction: @Composable () -> Unit by mutableStateOf({})
    var navigationIcon: @Composable () -> Unit by mutableStateOf({})
    private var image by mutableStateOf("http://jwxt.jmu.edu.cn")
    var title by mutableStateOf("JMU Assistant")
    var basicInfo by mutableStateOf("")
    var startRoute by mutableStateOf(BtmNav.Func.route)
    private fun context(): Context = getApplication<Application>().applicationContext

    suspend fun judgeStartRoute(dataStore: DataStore<Preferences>) {
        MainActivity.cookie = dataStore.data.first()[COOKIE_KEY] ?: ""
        if (MainActivity.cookie.isEmpty()) {
            startRoute = ContentNav.Login.route
            return
        }

        getStudentId()

    }

    suspend fun getStudentId() {
        try {
            val response = TheRetrofit.api.getStudentId().awaitResponse()
            val redirect = response.headers()["Location"] ?: ""

            if (redirect.isBlank()) {
                response.body()?.let {
                    val jsoup = Jsoup.parse(it)
                    studentID = jsoup.select(".footer.btn.btn-info")[0].attr("value").toString()
                    Log.d("StudentId", studentID)
                }
            }

            if (studentID.isNotBlank()) return

            if (redirect.contains("login?refer")) {
                startRoute = ContentNav.Login.route
            } else {
                startRoute = BtmNav.Func.route
                studentID = redirect.substringAfter("/info/")
                Log.d("StudentID", studentID)
            }
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }

    fun getImageRequest() = ImageRequest.Builder(context())
        .data(image)
        .setHeader("Cookie", MainActivity.cookie)
        .transformations(RoundedCornersTransformation(topLeft = 25f, bottomLeft = 25f))
        .build()

    suspend fun getStudentInfo() {
        try {
            val response = TheRetrofit.api.getStudentInfo(studentID).awaitResponse()
            val jsoup = Jsoup.parse(response.body().toString())
            rightData +=
                jsoup.getElementsByClass("base-header-right")[0].text()
                    .replace(Regex("(?=([男女学]|(证件号)))|((证件类型).*?(?=\\s) )"), "\n")
            image = "http://jwxt.jmu.edu.cn" + jsoup.getElementsByTag("img")[0].attr("src")
            val tds = jsoup.getElementsByClass("table-three-bisection")[0].getElementsByTag("td")
            repeat(tds.size / 2) { basicInfo += "${tds[it * 2].html()} : ${tds[it * 2 + 1].html()}\n" }
            showInfo = true
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }


}