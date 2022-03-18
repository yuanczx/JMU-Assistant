package com.jmu.assistant.utils.page

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jmu.assistant.models.SchoolMesseage
import com.jmu.assistant.utils.HttpTool
import com.jmu.assistant.utils.awaitResponse
import com.jmu.assistant.utils.buildReuest
import org.jsoup.Jsoup

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@ExperimentalFoundationApi
class MesseagePageSource(private val msgType: MsgType) : PagingSource<Int, SchoolMesseage>() {
    private var maxIndex = 476
    override fun getRefreshKey(state: PagingState<Int, SchoolMesseage>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SchoolMesseage> {
        val index = params.key ?: 0
        val preKey = if (index == 0) null else index - 1
        val nextKey = if (index >= maxIndex - 2) null else index + 1
        return try {
            val data = getData(index)
            LoadResult.Page(data = data, prevKey = preKey, nextKey = nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun getData(index: Int = 0): List<SchoolMesseage> {

        //获取request
        val request =
            if (index == 0) buildReuest("${msgType.url}.htm")
            else buildReuest("${msgType.url}/${maxIndex - index}.htm")

        val response = HttpTool.client.newCall(request).awaitResponse()
        val data = mutableListOf<SchoolMesseage>()
        val document = Jsoup.parse(response.body!!.string())


        //获取数据页数
        if (index == 0) {
            maxIndex = document.getElementsByClass("Next")[0].attr("href").let {
                val max = it.substring(it.indexOf("/") + 1, it.indexOf(".htm"))
                (max.toIntOrNull() ?: 2) + 1
            }
        }

        //获取数据List
        if (msgType == MsgType.SchoolNews || msgType == MsgType.SchoolNote) {
            document.getElementsByClass("con-list fl")[0]
                .getElementsByTag("ul")[0]
                .getElementsByTag("li").let {
                    for (ele in it) {
                        data += SchoolMesseage(
                            link = ele.getElementsByTag("a")[0].attr("href")
                                .replace("../../", "https://www.jmu.edu.cn/")
                                .replace("../", "https://www.jmu.edu.cn/"),
                            title = ele.getElementsByTag("a")[0].html(),
                            time = ele.getElementsByTag("p")[0].html()
                        )
                    }
                }
        } else {
            document.getElementsByTag("tbody")[1]
                .getElementsByAttributeValueContaining("height", "30").let {

                    for (ele in it) {
                        var link = ele.getElementsByClass("c4222")[0].attr("href")
                            .replace("../../", "https://jwc.jmu.edu.cn/")
                            .replace("../", "https://jwc.jmu.edu.cn/")
                        if (!link.contains("http")){link = "https://jwc.jmu.edu.cn/$link"}
                        data += SchoolMesseage(
                            link = link,
                            title = ele.getElementsByClass("c4222")[0].attr("title"),
                            time = ele.getElementsByClass("timestyle4222").html()
                                .replace("&nbsp;", "")
                        )
                    }
                }
        }
        return data
    }
}