package com.jmu.assistant.models

data class CourseTable(val studentTableVm: StudentTableVm)

data class StudentTableVm(
    val name: String,
    val activities: ArrayList<Activity>
)

data class Activity(
    val courseName: String,
    val weekIndexes: IntArray,
    val room: String?,
    val building: String,
    val campus: String,
    val weekday: Int,
    val startUnit: Int,
    val endUnit: Int,
    val teachers: ArrayList<String>
)
