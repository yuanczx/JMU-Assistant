package com.yuan.transcript.models

data class Activity(
    val lessonId:Int,
    val lessonName:String,
    val courseName:String,
    val weekIndexes:IntArray,
    val room:String,
    val building:String,
    val campus:String,
    val weekday:Int,
    val startUnit:Int,
    val endUnit:Int,
    val teachers:ArrayList<String>
)
