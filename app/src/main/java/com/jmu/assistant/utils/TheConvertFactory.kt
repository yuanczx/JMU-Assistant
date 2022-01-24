package com.jmu.assistant.utils

import android.util.Log
import com.google.gson.Gson
import com.jmu.assistant.models.CourseTable
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.HEAD
import retrofit2.http.Header
import java.lang.reflect.Type

class TheConvertFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return null
    }

}

class CourseConverter : Converter<ResponseBody, CourseTable> {
    override fun convert(value: ResponseBody): CourseTable? {
        Log.d("json", value.string())
        return Gson().fromJson(value.string(), CourseTable::class.java)
    }

}