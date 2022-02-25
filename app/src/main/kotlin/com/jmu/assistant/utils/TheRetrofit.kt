package com.jmu.assistant.utils

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
object TheRetrofit {
    private const val BASE_URL = "http://jwxt.jmu.edu.cn/student/for-std/"
    val client by lazy {
        OkHttpClient.Builder()
            .callTimeout(40, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .followRedirects(false)
            .addInterceptor(RequestInterceptor())
            .build()
    }

    private val gson by lazy { GsonBuilder().serializeNulls().setLenient().create() }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(TheConvertFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    val api: Apis by lazy {
        retrofit.create(Apis::class.java)
    }
}