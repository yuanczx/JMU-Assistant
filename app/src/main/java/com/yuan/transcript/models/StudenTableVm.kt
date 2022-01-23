package com.yuan.transcript.models

data class StudenTableVm(
    val id: Int,
    val name: String,
    val code: String,
    val grade: String,
    val department: String,
    val adminclass:String,
    val activities:ArrayList<Activity>
)
