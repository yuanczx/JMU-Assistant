package com.jmu.assistant.models

import com.google.gson.annotations.SerializedName

data class Transcript(val semesterId2studentGrades: Semester)
data class Semester(
    @SerializedName("41") val semester_20_21_1: ArrayList<CourseInfo>?,
    @SerializedName("42") val semester_20_21_2: ArrayList<CourseInfo>?,
    @SerializedName("61") val semester_21_22_1: ArrayList<CourseInfo>?,
    @SerializedName("181") val semester_21_22_2: ArrayList<CourseInfo>?
)

data class CourseInfo(val course:Course,val gaGrade:String,val passed:Boolean,val gp:Float,val published:Boolean)
data class Course(val nameZh:String,val credits: Float)