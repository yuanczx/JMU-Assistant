package com.jmu.assistant

import com.google.gson.Gson
import com.jmu.assistant.models.CourseTable
import org.junit.Test

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
    fun courseTable() {
        val tableJson = """
            {
                "studentTableVm": {
                    "activities": [
                        {
                            "courseName": "数学建模",
                            "weekIndexes": [
                                1,
                                2,
                                6,
                                7,
                                9,
                                11,
                                12
                            ],
                            "room": "建发0220*",
                            "building": "建发楼",
                            "campus": "主校区",
                            "weekday": 5,
                            "startUnit": 9,
                            "endUnit": 10,
                            "lessonRemark": null,
                            "teachers": [
                                "赵玲"
                            ]
                        },
                        {
                            "courseName": "数学建模",
                            "weeksStr": "4",
                            "weekIndexes": [
                                4
                            ],
                            "room": "建发0304*",
                            "building": "建发楼",
                            "campus": "主校区",
                            "weekday": 2,
                            "startUnit": 7,
                            "endUnit": 8,
                            "lessonRemark": null,
                            "teachers": [
                                "赵玲"
                            ]
                        },
                        {
                            "courseName": "数学建模",
                            "weekIndexes": [
                                4
                            ],
                            "room": "建发0302*",
                            "building": "建发楼",
                            "campus": "主校区",
                            "weekday": 5,
                            "startUnit": 9,
                            "endUnit": 10,
                            "teachers": [
                                "赵玲"
                            ]
                        }
                    ]
                }
            }
        """.trimIndent()
        val courses = Gson().fromJson(tableJson, CourseTable::class.java)
        courses?.let { ct ->
            ct.studentTableVm.activities.forEach { course ->
                val gap = course.weekIndexes.copyOf()//差值
                val counts = course.weekIndexes.copyOf()//循环次数
                var count = 1//计数


                //计算gap

                for (index in counts.indices) {
                    if (index < counts.size - 1) {
                        gap[index] = course.weekIndexes[index + 1] - course.weekIndexes[index]
                    } else
                        if (index != 0) gap[index] = gap[index - 1]
                }

                println(gap.contentToString())

                //计算counts
                for (index in gap.indices) {
                    if (index == gap.size - 1) {
                        counts[index] = 0; break
                    }


                    if (gap[index] == gap[index + 1] && gap.size > 1) {
                        counts[index] = 0
                        counts[index - count + 1] = ++count
                    } else {
                        count = 1
                        counts[index] = 1
                        if (index > 0 && gap[index - 1] == gap[index]) counts[index] = 0
                    }
                }
                println(counts.contentToString())
            }
        }
    }


}