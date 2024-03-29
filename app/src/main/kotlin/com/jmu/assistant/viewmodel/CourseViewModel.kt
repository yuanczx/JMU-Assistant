package com.jmu.assistant.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.models.Activity
import com.jmu.assistant.models.CourseTable
import com.jmu.assistant.utils.HttpTool
import retrofit2.awaitResponse
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*

@ExperimentalFoundationApi
@ExperimentalAnimationApi
class CourseViewModel(application: Application) : BaseViewModel(application) {
    private val gson = Gson()

    @SuppressLint("SdCardPath")
    private val jsonFile = File("/data/data/com.jmu.assistant/files/Course.json")

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
    }


    var handle by mutableStateOf(false)
    private val weekDay = arrayOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")
    private val classTime = listOf(
        "080000", "085000", "100500", "105500", "140000",
        "145000", "155500", "164500", "190000", "195000"
    )
    private val quitTime = listOf(
        "084500", "093500", "105000", "114000", "144500",
        "153500", "164000", "173000", "194500", "203500"
    )
    private val semesters = listOf(201, 181, 61)
    val courseTime = listOf(
        "8:00\n8:45", "8:50\n9:35",
        "10:05\n10:50", "10:55\n11:40",
        "14:00\n14:45", "14:50\n15:35",
        "15:55\n16:40", "16:45\n17:30",
        "19:00\n19:45", "19:50\n20:35"
    )


    var courseName by mutableStateOf("")
    var room by mutableStateOf("")
    var teacher by mutableStateOf("")

    val weekDayName = listOf(
        getString(R.string.Monday),
        getString(R.string.Tuesday),
        getString(R.string.Wednesday),
        getString(R.string.Thursday),
        getString(R.string.Friday),
        getString(R.string.Saturday),
        getString(R.string.Sunday),
    )

    private val startDate = listOf(
        LocalDate.parse("2022-08-28"),
        LocalDate.parse("2022-02-20"),
        LocalDate.parse("2021-09-05")
    )

    var showWeekend by mutableStateOf(false)
    val semesterItem = listOf(
        getString(R.string.first_semester_22_23),
        getString(R.string.second_semester_21_22),
        getString(R.string.first_semester_21_22),
    )

    var addCourseDialog by mutableStateOf(false)
    var currentSelect by mutableStateOf(Pair(0, 0))
    var weekSelector by mutableStateOf(1)
    var loadFinish by mutableStateOf(false)
    private var ics by mutableStateOf("")
    var semesterIndex by mutableStateOf(0)
    private var courseTable: CourseTable? = null
    var loadCourse by mutableStateOf(false)

    //课表结构：                 周次            [星期        课程名字  教室     教师]
    val weekCourse: MutableMap<Int, MutableMap<Int, Triple<String, String, String>>> =
        mutableStateMapOf()

    fun calculateWeek(): Int {
        /**
         * @Author yuanczx
         * @Description 计算当前周次
         * @Date 2022/3/27 10:25
         * @Params []
         * @Return 当前周次
         **/
        val weeks = ChronoUnit.WEEKS.between(startDate[semesterIndex].plusDays(1), LocalDate.now())
        if (weeks >= 19 || weeks <= 1) {
            return 1
        }
        return (weeks + 1).toInt()
    }

    fun makeCourseTable(week: Int = 0) {
        /**
         * @Author yuanczx
         * @Description 生成课表
         * @Date 2022/3/27 10:26
         * @Params [week]
         * @Return
         **/
        weekSelector = if (week == 0) calculateWeek() else week

        courseTable?.let { ct ->
            repeat(7) {
                val courseMap = mutableStateMapOf<Int, Triple<String, String, String>>()
                ct.studentTableVm.activities.forEach { activity ->
                    if (activity.weekday == it + 1 && activity.weekIndexes.contains(weekSelector)) {
                        if (!handle) showWeekend = (activity.weekday > 5)
                        courseMap[activity.startUnit] = Triple(
                            activity.courseName,
                            (activity.room ?: "").replace("*", "")
                                .replaceFirst(Regex("(?=\\d)"), "\n"),
                            activity.teachers.first()
                        )
                    }
                }
                weekCourse[it + 1] = courseMap
            }
        }
    }


    @ExperimentalMaterial3Api
    suspend fun getCourseTable(refresh: Boolean = false) {
        loadCourse = true
        if (jsonFile.exists() && !refresh) {
            courseTable = gson.fromJson(jsonFile.readText(), CourseTable::class.java)
            loadCourse = false
            makeCourseTable()
            return
        }
        try {
            val response = HttpTool.api.getCourse(
                semester = semesters[semesterIndex],
                studentId = MainActivity.studentID
            ).awaitResponse()
            if (response.isSuccessful) {
                courseTable = response.body()
                courseTable?.let { course ->
                    course.studentTableVm.activities.forEach {
                        it.weekIndexes.sort()
                    }
                }
                loadCourse = false
            }
        } catch (e: Exception) {
            loadCourse = false
            Log.e(e.toString(), e.message.toString())
            toast(R.string.request_fail)
        }
        courseToJson()
    }

    @SuppressLint("SdCardPath")
    fun exportICS() {
        try {
            val fileDire = File("/data/data/com.jmu.assistant/files/CourseTable")
            fileDire.deleteRecursively()
            fileDire.mkdirs()
            val icsFile = File("${fileDire.path}/Course-${LocalTime.now()}.ics")
            icsFile.createNewFile()
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.action = Intent.ACTION_VIEW
            intent.data =
                FileProvider.getUriForFile(context(), "com.jmu.assistant.fileprovider", icsFile)
            icsFile.writeText(ics)
            toast(R.string.select_app_toast)
            context().startActivity(intent)
        } catch (e: Exception) {
            toast(R.string.export_ics_error)
            Log.d("StartActivity", e.message.toString())
        }
    }

    fun buildICS() {
        ics = ICS_START.trimIndent()
        courseTable?.let { ct ->
            ct.studentTableVm.activities.forEach { course ->
                val gap = course.weekIndexes.toIntArray()//差值
                val counts = course.weekIndexes.toIntArray()//循环次数
                var count = 1//计数
                if (gap.size == 1) {
                    counts[0] = 1
                } else {
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
                }
                //拼接ICS文件
                ics += makeEventWithWeeks(
                    startDate[semesterIndex],
                    counts = counts,
                    gap = gap,
                    course = course
                )
            }
            ics += "\nEND:VCALENDAR"//ics结束语句
            ics = ics.replace(Regex("\\s+"), "\n") //去除多余空白符
        }
    }


    private fun makeEventWithWeeks(
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
                UID:yuanczx@${UUID.randomUUID()}
                DTSTART;TZID=Asia/Shanghai:${
                    date.toString().replace("-", "")
                }T${classTime[course.startUnit - 1]}
                RRULE:FREQ=WEEKLY;INTERVAL=${if (counts[index] >= 2) gap[index] else "1"};BYDAY=${weekDay[course.weekday - 1]};COUNT=${counts[index]}
                DTEND;TZID=Asia/Shanghai:${
                    date.toString().replace("-", "")
                }T${quitTime[course.endUnit - 1]}
                SUMMARY:${course.courseName.replace(Regex("\\s"), "")}
                DESCRIPTION:${
                    course.teachers.toArray().contentToString().replace("[", "").replace("]", "")
                }
                LOCATION:${
                    try {
                        course.room?.removeSuffix("*")
                    } catch (e: NullPointerException) {
                        course.room
                    }
                }
                END:VEVENT """.trimIndent()
            }
        }
        return event
    }

    fun addCourse() {
        courseTable!!.studentTableVm.activities.find {
            it.courseName == courseName
                    && it.startUnit == currentSelect.second * 2 + 1
                    && it.room == room
                    && it.teachers.contains(teacher)
        }?.let {
            it.weekIndexes.add(weekSelector)
            courseToJson()
            return
        }
        courseTable!!.studentTableVm.activities.add(
            Activity(
                courseName = courseName,
                weekIndexes = arrayListOf(weekSelector),
                weekday = currentSelect.first + 1,
                room = room,
                startUnit = currentSelect.second * 2 + 1,
                endUnit = currentSelect.second * 2 + 2,
                teachers = arrayListOf(teacher)
            )
        )
        courseToJson()
    }

    private fun courseToJson() {
        makeCourseTable(weekSelector)
        try {
            if (!jsonFile.exists()) jsonFile.createNewFile()
            val text = gson.toJson(courseTable)
            jsonFile.writeText(text)
        } catch (e: IOException) {
            Log.e("WriteJson", "addCourse: ${e.message}")
        }
    }

    fun isCourseExist() {
        weekCourse[currentSelect.first + 1]?.get(currentSelect.second * 2 + 1)?.let {
            courseName = it.first.replace(Regex("\\s"), "")
            room = it.second.replace(Regex("\\s"), "")
            teacher = it.third.replace(Regex("\\s"), "")
            return
        }
        courseName = ""
        teacher = ""
        room = ""
    }

    fun deleteCourse() {
        addCourseDialog = false
        courseTable!!.studentTableVm.activities.find {
            it.courseName == weekCourse[currentSelect.first + 1]?.get(currentSelect.second * 2 + 1)?.first
                    && it.weekday == currentSelect.first + 1
                    && it.weekIndexes.contains(weekSelector)
                    && it.startUnit == currentSelect.second * 2 + 1
        }?.let {
            if (it.weekIndexes.size == 1) {
                courseTable!!.studentTableVm.activities.remove(it)
            } else {
                it.weekIndexes.remove(weekSelector)
            }
        }
        courseToJson()
    }


}