package com.jmu.assistant

import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleUnitTest {
    @Test
    fun login() {
        /**
         * @Author yuanczx
         * @Description 登录测试
         * @Date 2022/3/11 16:48
         * @Params []
         * @Return
         **/
        println("Test Login")
    }

    @Test
    fun week() {
        val startDate = listOf(LocalDate.parse("2022-02-20"), LocalDate.parse("2021-09-05"))
        val weeks = ChronoUnit.WEEKS.between(startDate[0].plusDays(1),LocalDate.now())
        println(weeks)
    }


    @Test
    fun testTrim(){
        val text = "Hello\nworld"
        println(text.replace(Regex("\\s"),""))
    }
}