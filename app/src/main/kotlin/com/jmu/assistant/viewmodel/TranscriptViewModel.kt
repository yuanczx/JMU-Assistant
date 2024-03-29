package com.jmu.assistant.viewmodel

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jmu.assistant.MainActivity
import com.jmu.assistant.models.CourseInfo
import com.jmu.assistant.models.Transcript
import com.jmu.assistant.utils.HttpTool
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import retrofit2.awaitResponse


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
class TranscriptViewModel : ViewModel() {
    var loading by mutableStateOf(false)
    var error by mutableStateOf(false)
    var showTabs by mutableStateOf(false)
    var selectedTab by mutableStateOf(0)
    var semesters: Elements? = null
    var semesterId by mutableStateOf("")
    var transcript: Transcript? by mutableStateOf(null)


    suspend fun getGrade() {
        try {
            val response = HttpTool.api.getTranscript(MainActivity.studentID).awaitResponse()
            if (response.isSuccessful) {
                transcript = response.body()
//                transcript = Gson().fromJson(transcriptJson,Transcript::class.java)
                transcript?.semesterId2studentGrades?.semester_21_22_1?.get(0)?.course?.let {
                    Log.d("json", it.nameZh)
                }
            }
        } catch (e: Exception) {
            error = true
            Log.e(e.cause.toString(), e.message.toString())
        } finally {
            loading = false
        }
    }

    fun semesterSelector(): ArrayList<CourseInfo>? = transcript?.semesterId2studentGrades?.run {
        when (semesterId) {
            "41" -> semester_20_21_1
            "42" -> semester_20_21_2
            "61" -> semester_21_22_1
            "181"-> semester_21_22_2
            "40" -> semester_19_20_2
            "39" -> semester_19_20_1
            "38" -> semester_18_19_2
            "37" -> semester_18_19_1
            else -> semester_21_22_2
        }
    }

    suspend fun getSemesterIndex() {
        try {
            val response = HttpTool.api.getSemesterIndex(MainActivity.studentID).awaitResponse()
            val jsoup = Jsoup.parse(response.body().toString())
            val temp = jsoup.getElementById("semester")?.getElementsByTag("option")
            temp?.let {
                it.removeAt(0)//移除元素 : ...
                semesters = it
                semesterId = it[0].`val`()
            }
            showTabs = true
        } catch (e: Exception) {
            error = true
            Log.e(e.cause.toString(), e.message.toString())
            loading = false
        }
    }

}