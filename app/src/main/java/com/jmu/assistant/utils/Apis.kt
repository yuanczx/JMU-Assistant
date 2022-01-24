package com.jmu.assistant.utils

import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.models.CourseTable
import com.jmu.assistant.models.Transcript
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Apis {
    @GET("course-table/semester/{semester}/print-data/0/{studentId}")
    fun getCourse(@Path("semester") semester:Int,@Path("studentId") studentId:String):Call<CourseTable>
    @GET("grade/sheet/info/{studentId}?semester=")
    fun getTranscript(@Path("studentId") studentId: String):Call<Transcript>
    @GET("student-info")
    fun getStudentId():Call<Any?>
}