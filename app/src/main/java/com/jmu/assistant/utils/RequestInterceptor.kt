package com.jmu.assistant.utils

import com.jmu.assistant.MainActivity
import okhttp3.Interceptor
import okhttp3.Response
import org.jetbrains.annotations.TestOnly

class RequestInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
       val builder = chain.request().newBuilder()
        builder.header("Cookie", MainActivity.cookie)
        return chain.proceed(builder.build())
    }
}