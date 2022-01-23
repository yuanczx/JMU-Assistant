package com.yuan.transcript.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.yuan.transcript.models.Activity
import com.yuan.transcript.models.CourseTable
import okhttp3.*
import java.io.IOException
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class CourseViewModel:ViewModel() {
    companion object {

        const val BASE_URL_COURSE_TABLE =
            "http://jwxt.jmu.edu.cn/student/for-std/course-table/semester/"
        const val ICS_START =
            """BEGIN:VCALENDAR VERSION:2.0 
                PRODID:yuanczx BEGIN:VTIMEZONE 
                TZID:Asia/Shanghai 
                TZURL:http://tzurl.org/zoneinfo-outlook/Asia/Shanghai
                X-LIC-LOCATION:Asia/Shanghai
                BEGIN:STANDARD
                TZNAME:CST
                TZOFFSETFROM:+0800
                TZOFFSETTO:+0800
                DTSTART:19700101T000000
                END:STANDARD
                END:VTIMEZONE   """
        val WEEKDAY = arrayOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")
        val CLASS_TIME = listOf(
            "080000",
            "085000",
            "100500",
            "105500",
            "140000",
            "145000",
            "155500",
            "164500",
            "190000",
            "1950000"
        )
        val QUITE_TIME = listOf(
            "084500",
            "093500",
            "105000",
            "114000",
            "144500",
            "153500",
            "164000",
            "173000",
            "194500",
            "203000"
        )
        val SEMESTER = listOf(181,61)
    }

    var loadFinish by mutableStateOf(false)
    lateinit var cookie:String
    val gson = Gson()
    var ics by mutableStateOf(ICS_START.trimIndent())
    var semesterIndex by mutableStateOf(0)
    var studentId = 12267
    var courseTable:CourseTable? by mutableStateOf(null)

    var loadCourse by mutableStateOf(false)
    private val client = OkHttpClient.Builder()
        .callTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()


    private fun buildRequest() = Request.Builder()
        .url("${BASE_URL_COURSE_TABLE}${SEMESTER[semesterIndex]}/print-data/0/$studentId")
        .header("Cookie", cookie)
        .get()
        .build()

    fun getCourseTable() {
        loadCourse = true
        client.newCall(buildRequest()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("get_course_table", e.message!!)
                loadCourse = false
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                courseTable = gson.fromJson(response.body!!.string(), CourseTable::class.java)
                buildICS()
                loadCourse = false
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun buildICS() {
        courseTable?.let { ct ->
            ct.studentTableVm.activities.forEach { course ->
                val gap = course.weekIndexes.copyOf()//差值
                val counts = course.weekIndexes.copyOf()//循环次数
                var count = 1//计数

                //计算gap
                for (index in counts.indices) {
                    if (index < counts.size-1) {
                        gap[index] = course.weekIndexes[index + 1] - course.weekIndexes[index] }
                    else
                        if (index!=0) gap[index] = gap[index - 1]
                }

                //计算counts
                for (index in gap.indices){
                    if (index==gap.size-1){counts[index]=0; break}

                    if (gap[index]==gap[index+1]){
                        counts[index] = 0
                        counts[index-count+1] = ++count
                    }else{
                        count=1
                        counts[index] = 1
                        if (index>0 && gap[index-1]==gap[index]) counts[index] = 0
                    }
                }
                //拼接ICS文件
                ics +=makeEventWithWeeks(LocalDate.parse("2022-02-20"), counts = counts, gap = gap, course = course)
                //调试输出
                Log.d("counts:${course.courseName}", counts.contentToString())
                Log.d("gap:${course.courseName}", gap.contentToString())
                Log.d("indexes:${course.courseName}", course.weekIndexes.contentToString())
            }
            ics += "\nEND:VCALENDAR"
            ics = ics.replace(Regex("\\s+"), "\n")
            loadFinish = true
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun makeEventWithWeeks(
        firstWeek: LocalDate,
        gap: IntArray,
        counts: IntArray,
        course: Activity
    ): String {
        var event = ""
        for (index in course.weekIndexes.indices) {
            if (counts[index] != 0) {
                val date = firstWeek.plusDays((7 * (course.weekIndexes[index] - 1) + course.weekday).toLong())
                event += """    BEGIN:VEVENT
                UID:@yuanczx${UUID.randomUUID()}
                DTSTART;TZID=Asia/Shanghai:${
                    date.toString().replace("-", "")
                }T${CLASS_TIME[course.startUnit - 1]}
                RRULE:FREQ=WEEKLY;INTERVAL=${if (counts[index] >= 2) gap[index] else "1"};BYDAY=${WEEKDAY[course.weekday - 1]};COUNT=${counts[index]}
                DTEND;TZID=Asia/Shanghai:${
                    date.toString().replace("-", "")
                }T${QUITE_TIME[course.endUnit - 1]}
                SUMMARY:${course.courseName.replace(Regex("\\s"),"")}
                DESCRIPTION:${course.teachers.toArray().contentToString().replace("[","").replace("]","")}
                LOCATION:${course.room}
                END:VEVENT """.trimIndent()
            }
        }
        return event
    }
}