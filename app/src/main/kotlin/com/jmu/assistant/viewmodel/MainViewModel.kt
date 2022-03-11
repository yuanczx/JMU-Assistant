package com.jmu.assistant.viewmodel

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.jmu.assistant.MainActivity
import com.jmu.assistant.MainActivity.Companion.COOKIE_KEY
import com.jmu.assistant.MainActivity.Companion.studentID
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.utils.HttpTool
import kotlinx.coroutines.flow.first
import org.jsoup.Jsoup
import retrofit2.awaitResponse

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class MainViewModel : ViewModel() {

    var startRoute by mutableStateOf(ContentNav.Menu.route)

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
            val response = HttpTool.api.getStudentId().awaitResponse()
            val redirect = response.headers()["Location"] ?: ""

            if (redirect.isBlank()) {
                response.body()?.let {
                    val jsoup = Jsoup.parse(it)
                    studentID = jsoup.select(".footer.btn.btn-info")[0].attr("value").toString()
                    Log.d("StudentId", studentID)
                    return
                }
            }


            if (redirect.contains("login?refer")) {
                startRoute = ContentNav.Login.route
            } else {
                startRoute = ContentNav.Menu.route
                studentID = redirect.substringAfter("/info/")
                Log.d("StudentID", studentID)
            }
        } catch (e: Exception) {
            startRoute = ContentNav.Login.route
            Log.e(e.toString(), e.message.toString())
        }
    }


}