package com.jmu.assistant.utils

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class TheConvertFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type == String::class.java) return StringConverter()
        return null
    }

}

class StringConverter : Converter<ResponseBody, String> {
    override fun convert(value: ResponseBody): String {
        return value.string()
    }
}