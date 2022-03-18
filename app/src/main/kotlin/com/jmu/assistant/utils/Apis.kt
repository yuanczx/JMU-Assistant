package com.jmu.assistant.utils

import com.jmu.assistant.models.CourseTable
import com.jmu.assistant.models.Transcript
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Apis {
    @GET("course-table/semester/{semester}/print-data/0/{studentId}")
    fun getCourse(@Path("semester") semester: Int, @Path("studentId") studentId: String): Call<CourseTable>

    @GET("grade/sheet/info/{studentId}")
    fun getTranscript(@Path("studentId") studentId: String,@Query("semester") semester: String=""): Call<Transcript>

    @GET("student-info")
    fun getStudentId(): Call<String?>

    @GET("grade/sheet/semester-index/{studentId}")
    fun getSemesterIndex(@Path("studentId") studentId: String):Call<String>

    @GET("student-info/info/{studentId}")
    fun getStudentInfo(@Path("studentId") studentId: String): Call<String>

    @GET("program/root-module-json/{studentID}")
    fun getTrainData(@Path("studentID") studentId: String):Call<String>
}