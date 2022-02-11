package com.jmu.assistant

import com.jmu.assistant.utils.TheRetrofit
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testGetStudentId(){
        val id = TheRetrofit.api.getStudentId().execute().headers()["Location"]
        print(id)
    }
}