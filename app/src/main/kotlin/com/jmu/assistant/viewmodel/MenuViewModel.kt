package com.jmu.assistant.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.jmu.assistant.BuildConfig
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.utils.HttpTool
import com.jmu.assistant.utils.awaitResponse
import kotlinx.coroutines.launch
import okhttp3.Request
import okio.IOException
import org.jsoup.Jsoup
import retrofit2.awaitResponse

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class MenuViewModel(application: Application) : BaseViewModel(application) {
    companion object {
        private const val UPDATE_URL = "https://yuanczx.github.io/post/jmua_update/"
        private const val TAG = "Update"
    }

    var rightData by mutableStateOf("")
    var basicInfo by mutableStateOf("")

    var versionName by mutableStateOf("") //版本名
    var description by mutableStateOf("") //更新描述
    private var download = "" //下载地址
    var newVersion by mutableStateOf(false) // 新版本

    var checkingUpdate by mutableStateOf(false)
    private var image by mutableStateOf("http://jwxt.jmu.edu.cn")

    fun checkUpdate(showDialog: Boolean = true) {
        /**
         * @Author yuanczx
         * @Description 检查更新
         * @Date 2022/3/11 21:04
         * @Params [showDialog]
         * @Return
         **/
        checkingUpdate = showDialog
        val request = Request.Builder().url(UPDATE_URL).build()
        viewModelScope.launch {
            try {
                HttpTool.client.newCall(request).awaitResponse()
                    .body?.let {
                        val updateInfo = Jsoup.parse(it.string())
                        updateInfo.select("br").append("\\n")
                        val versionCode =
                            updateInfo.getElementById("versionCode")?.text()?.toInt() ?: 100
                        checkingUpdate = false
                        if (versionCode <= BuildConfig.VERSION_CODE) {
                            if (showDialog) toast(R.string.newest)
                        } else {
                            versionName =
                                updateInfo.getElementById("versionName")?.text().toString()
                            description =
                                (updateInfo.getElementById("description")?.text() ?: "").replace(
                                    Regex("\\\\n\\s?"),
                                    "\n"
                                )
                            download = updateInfo.getElementById("download")?.text().toString()
                            Log.d(TAG, "checkUpdate: $download")
                            newVersion = true
                        }
                    }
            } catch (e: Exception) {
                Log.d(TAG, "checkUpdate: ${e.message}")
                checkingUpdate = false
                newVersion = false
            } catch (e: IOException) {
                toast(R.string.network_error)
            }
        }
    }


    fun getImageRequest() = ImageRequest.Builder(context())
        .data(image)
        .setHeader("Cookie", MainActivity.cookie)
        .transformations(RoundedCornersTransformation(topLeft = 25f, bottomLeft = 25f))
        .build()

    suspend fun getStudentInfo() {
        /**
         * @Author yuanczx
         * @Description 获取学籍信息
         * @Date 2022/3/11 21:03
         * @Params []
         * @Return
         **/
        try {
            val response = HttpTool.api.getStudentInfo(MainActivity.studentID).awaitResponse()
            val jsoup = Jsoup.parse(response.body().toString())
            rightData +=
                jsoup.getElementsByClass("base-header-right")[0].text()
                    .replace(Regex("(?=([男女学]|(证件号)))|((证件类型).*?(?=\\s) )"), "\n")
            image = "http://jwxt.jmu.edu.cn" + jsoup.getElementsByTag("img")[0].attr("src")
            val tds = jsoup.getElementsByClass("table-three-bisection")[0].getElementsByTag("td")
            repeat(tds.size / 2) { basicInfo += "${tds[it * 2].html()} : ${tds[it * 2 + 1].html()}\n" }
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }

    fun update() {
        /**
         * @Author yuanczx
         * @Description 升级版本
         * @Date 2022/3/11 21:03
         * @Params []
         * @Return
         **/
        newVersion = false
        checkingUpdate = false
        if (download.isNotBlank()) {
            try {
                val uri = Uri.parse(download)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context().startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "update: ${e.message}")
            }
        }
    }
}
