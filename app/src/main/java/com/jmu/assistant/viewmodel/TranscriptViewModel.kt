package com.jmu.assistant.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jmu.assistant.MainActivity
import com.jmu.assistant.models.CourseInfo
import com.jmu.assistant.models.Semester
import com.jmu.assistant.models.Transcript
import com.jmu.assistant.utils.TheRetrofit
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import retrofit2.awaitResponse


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
            val response = TheRetrofit.api.getTranscript(MainActivity.studentID).awaitResponse()
            if (response.isSuccessful) {
                transcript = response.body()
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
            else -> semester_21_22_2
        }
    }

    suspend fun getSemesterIndex() {
        try {
            val response = TheRetrofit.api.getSemesterIndex(MainActivity.studentID).awaitResponse()
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