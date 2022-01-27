package com.jmu.assistant.models

data class Student(
    val name: String,
    val sex: String="",
    val nation: String="",
    val birthDate: String="",
    val politicsStatus: String="",
    val studentNum:String,
    val grade:String,
    val studentKind:String,
    val institute:String,
    val major:String,
    val trainingScheme:String
)
