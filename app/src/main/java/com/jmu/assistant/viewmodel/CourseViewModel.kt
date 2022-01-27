package com.jmu.assistant.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.jmu.assistant.MainActivity
import com.jmu.assistant.models.Activity
import com.jmu.assistant.models.CourseTable
import com.jmu.assistant.utils.TheRetrofit
import retrofit2.awaitResponse
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.timerTask

class CourseViewModel(application: Application) : AndroidViewModel(application) {

    private fun context(): Context = getApplication<Application>().applicationContext

    companion object {

        private const val ICS_START =
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
        private val WEEKDAY = arrayOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")
        private val CLASS_TIME = listOf(
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
        private val QUITE_TIME = listOf(
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
        private val SEMESTER = listOf(181, 61)

        @RequiresApi(Build.VERSION_CODES.O)
        private val START_DATE =
            listOf(LocalDate.parse("2022-02-20"), LocalDate.parse("2021-09-05"))
    }

    var ics by mutableStateOf("")
    var semesterIndex by mutableStateOf(0)
    private var courseTable: CourseTable? by mutableStateOf(null)
    var loadCourse by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCourseTable() {
        loadCourse = true
        val response = TheRetrofit.api.getCourse(
            semester = SEMESTER[semesterIndex],
            studentId = MainActivity.studentID
        ).awaitResponse()
        try {
            if (response.isSuccessful) {
                courseTable = response.body()
                courseTable?.let { course ->
                    course.studentTableVm.activities.forEach {
                        it.weekIndexes.sort()
                    }
                }
                buildICS()
                loadCourse = false
            }
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
            Toast.makeText(context(), "请求失败请重新尝试", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buildICS() {
        ics = ICS_START.trimIndent()
        courseTable?.let { ct ->
            ct.studentTableVm.activities.forEach { course ->
                val gap = course.weekIndexes.copyOf()//差值
                val counts = course.weekIndexes.copyOf()//循环次数
                var count = 1//计数

                //计算gap
                for (index in counts.indices) {
                    if (index < counts.size - 1) {
                        gap[index] = course.weekIndexes[index + 1] - course.weekIndexes[index]
                    } else
                        if (index != 0) gap[index] = gap[index - 1]
                }

                //计算counts
                for (index in gap.indices) {
                    if (index == gap.size - 1) {
                        counts[index] = 0; break
                    }

                    if (gap[index] == gap[index + 1]) {
                        counts[index] = 0
                        counts[index - count + 1] = ++count
                    } else {
                        count = 1
                        counts[index] = 1
                        if (index > 0 && gap[index - 1] == gap[index]) counts[index] = 0
                    }
                }
                //拼接ICS文件
                ics += makeEventWithWeeks(
                    START_DATE[semesterIndex],
                    counts = counts,
                    gap = gap,
                    course = course
                )
                //调试输出
                Log.d("counts:${course.courseName}", counts.contentToString())
                Log.d("gap:${course.courseName}", gap.contentToString())
                Log.d("indexes:${course.courseName}", course.weekIndexes.contentToString())
            }
            ics += "\nEND:VCALENDAR"
            ics = ics.replace(Regex("\\s+"), "\n")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun makeEventWithWeeks(
        startDate: LocalDate,
        gap: IntArray,
        counts: IntArray,
        course: Activity
    ): String {
        var event = ""
        for (index in course.weekIndexes.indices) {
            if (counts[index] != 0) {
                val date =
                    startDate.plusDays((7 * (course.weekIndexes[index] - 1) + course.weekday).toLong())
                event += """    BEGIN:VEVENT
                UID:@yuanczx${UUID.randomUUID()}
                DTSTART;TZID=Asia/Shanghai:${
                    date.toString().replace("-", "")
                }T${CLASS_TIME[course.startUnit - 1]}
                RRULE:FREQ=WEEKLY;INTERVAL=${if (counts[index] >= 2) gap[index] else "1"};BYDAY=${WEEKDAY[course.weekday - 1]};COUNT=${counts[index]}
                DTEND;TZID=Asia/Shanghai:${
                    date.toString().replace("-", "")
                }T${QUITE_TIME[course.endUnit - 1]}
                SUMMARY:${course.courseName.replace(Regex("\\s"), "")}
                DESCRIPTION:${
                    course.teachers.toArray().contentToString().replace("[", "").replace("]", "")
                }
                LOCATION:${
                    try {
                        course.room.removeSuffix("*")
                    } catch (e: NullPointerException) {
                        course.room
                    }
                }
                END:VEVENT """.trimIndent()
            }
        }
        return event
    }

    fun toast(s: String) = Toast.makeText(context(),s,Toast.LENGTH_SHORT).show()
}