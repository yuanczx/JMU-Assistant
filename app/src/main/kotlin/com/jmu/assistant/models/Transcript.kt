package com.jmu.assistant.models

import com.google.gson.annotations.SerializedName

data class Transcript(val semesterId2studentGrades: Semester)

data class Semester(
    @SerializedName("201") val semester_22_23_1:ArrayList<CourseInfo>?,
    @SerializedName("41") val semester_20_21_1: ArrayList<CourseInfo>?,
    @SerializedName("42") val semester_20_21_2: ArrayList<CourseInfo>?,
    @SerializedName("61") val semester_21_22_1: ArrayList<CourseInfo>?,
    @SerializedName("181") val semester_21_22_2: ArrayList<CourseInfo>?,
    @SerializedName("40") val semester_19_20_2: ArrayList<CourseInfo>?,
    @SerializedName("39") val semester_19_20_1: ArrayList<CourseInfo>?,
    @SerializedName("38") val semester_18_19_2: ArrayList<CourseInfo>?,
    @SerializedName("37") val semester_18_19_1: ArrayList<CourseInfo>?,
)

data class CourseInfo(
    val course: Course,
    val gaGrade: String,
    val passed: Boolean,
    val gp: Float,
    val published: Boolean
)

data class Course(val nameZh: String, val credits: Float)