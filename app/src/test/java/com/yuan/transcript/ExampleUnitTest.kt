package com.yuan.transcript

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun getJSon(){
        val gson = Gson()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://jwxt.jmu.edu.cn/student/for-std/course-table/semester/181/print-data/0/12267")
            .get()
            .addHeader("Cookie","SESSION=2387c37a-d542-43dc-a7d1-be3e64945c4b; __pstsid__=fbfb8662ede7d9dc7450f1c90cb49506")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                print(response.body!!.string())
            }
        })

    }

    @Test
    fun testDate(){
        val formatter = DateTimeFormatter.ofPattern("yyMMdd-HHmmss")
        val date = LocalDateTime.now()

        print(date.toString())
    }

    @Test
    fun dateTest(){
        val weeks = intArrayOf(1,2,2,11,1)
        var date = LocalDate.parse("2022-02-20")
        date = date.plusDays((7*(weeks[0]-1)+5).toLong())
        print(date)
    }

}