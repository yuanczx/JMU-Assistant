package com.jmu.assistant.utils

import com.jmu.assistant.MainActivity
import okhttp3.Interceptor
import okhttp3.Response
import org.jetbrains.annotations.TestOnly

class RequestIntercepter:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
       val builder = chain.request().newBuilder()
        builder.header("Cookie", MainActivity.cookie)
//        builder.header("Cookie","SESSION=48f66d2e-73fe-47be-8fb3-b5ef454fd7c6; __pstsid__=e28aa1dcc2e80c807dde94981984cc9b")
        return chain.proceed(builder.build())
    }
}