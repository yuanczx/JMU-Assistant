package com.jmu.assistant.utils

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun Call.awaitResponse(): Response {
    /**
     * @Author yuanczx
     * @Description OkHttp 协程请求封装
     * @Date 2022/3/11 8:57
     * @Params []
     * @Return Response
     **/
    return suspendCancellableCoroutine {

        it.invokeOnCancellation {
            //当协程被取消的时候，取消网络请求
            cancel()
        }

        enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                it.resume(response)
            }
        })
    }
}