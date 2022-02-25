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
import com.jmu.assistant.utils.TheRetrofit
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
    var showInfo by mutableStateOf(false)
    var basicInfo by mutableStateOf("")

    var versionName by mutableStateOf("")
    var description by mutableStateOf("")
    private var download = ""
    var newVersion by mutableStateOf(false)

    var checkingUpdate by mutableStateOf(false)
    private var image by mutableStateOf("http://jwxt.jmu.edu.cn")

//    private fun getVersionCode(): Long {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            context().packageManager.getPackageInfo(context().packageName,PackageManager.GET_ACTIVITIES).longVersionCode
//        }else{
//            context().packageManager.getPackageInfo(context().packageName,0).versionCode.toLong()
//        }
//    }
    fun checkUpdate() {
        checkingUpdate = true
        val request = Request.Builder()
            .url(UPDATE_URL)
            .build()
        viewModelScope.launch {
            try {
                TheRetrofit.client.newCall(request).awaitResponse()
                    .body?.let {
                        val updateInfo = Jsoup.parse(it.string())
                        updateInfo.select("br").append("\\n")
                        val versionCode =
                            updateInfo.getElementById("versionCode")?.text()?.toInt()?:100
                        checkingUpdate = false
                        if (versionCode <= BuildConfig.VERSION_CODE) {
                            toast(R.string.newest)
                        } else {
                            versionName = updateInfo.getElementById("versionName")?.text().toString()
                            description =(updateInfo.getElementById("description")?.text()?:"").replace(Regex("\\\\n\\s?"),"\n")
                            download = updateInfo.getElementById("download")?.text().toString()
                            Log.d(TAG, "checkUpdate: $download")
                            newVersion = true
                        }
                    }
            } catch (e: Exception) {
                Log.d(TAG, "checkUpdate: ${e.message}")
                checkingUpdate = false
                newVersion = false
            }catch (e:IOException){
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
        try {
            val response = TheRetrofit.api.getStudentInfo(MainActivity.studentID).awaitResponse()
            val jsoup = Jsoup.parse(response.body().toString())
            rightData +=
                jsoup.getElementsByClass("base-header-right")[0].text()
                    .replace(Regex("(?=([男女学]|(证件号)))|((证件类型).*?(?=\\s) )"), "\n")
            image = "http://jwxt.jmu.edu.cn" + jsoup.getElementsByTag("img")[0].attr("src")
            val tds = jsoup.getElementsByClass("table-three-bisection")[0].getElementsByTag("td")
            repeat(tds.size / 2) { basicInfo += "${tds[it * 2].html()} : ${tds[it * 2 + 1].html()}\n" }
            showInfo = true
        } catch (e: Exception) {
            Log.e(e.toString(), e.message.toString())
        }
    }

    fun update() {
        newVersion = false
        checkingUpdate =false
        if (download.isNotBlank()){
            try {
                val uri = Uri.parse(download)
                val intent = Intent(Intent.ACTION_VIEW,uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context().startActivity(intent)
            }catch (e:Exception){
                Log.e(TAG, "update: ${e.message}")
            }
        }
    }
}
