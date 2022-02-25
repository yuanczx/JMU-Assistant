package com.jmu.assistant.utils

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import com.jmu.assistant.MainActivity
import okhttp3.Interceptor
import okhttp3.Response

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!chain.request().url.toString()
                .contains("jwxt.jmu.edu.cn")
        ) return chain.proceed(chain.request())
        val builder = chain.request().newBuilder()
        builder.header("Cookie", MainActivity.cookie)
        return chain.proceed(builder.build())
    }
}