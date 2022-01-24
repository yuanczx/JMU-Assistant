package com.jmu.assistant

import androidx.compose.runtime.rememberCoroutineScope
import com.google.gson.Gson
import com.jmu.assistant.utils.TheRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.junit.Test
import retrofit2.awaitResponse

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

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