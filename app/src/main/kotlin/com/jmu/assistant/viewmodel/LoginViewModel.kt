package com.jmu.assistant.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import com.jmu.assistant.R
import com.jmu.assistant.utils.awaitResponse
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val BASE_URL_LOGIN =
            "https://cas.paas.jmu.edu.cn/cas/login?service=http%3A%2F%2Fjwxt.jmu.edu.cn%2Fstudent%2Fsso%2Flogin"
        private const val TAG = "login"
        private const val FLAG_URL = "http://jwxt.jmu.edu.cn/student/home"
    }

    val userNameKey = stringPreferencesKey("userName")
    val passWordKey = stringPreferencesKey("password")
    var rememberPwd by mutableStateOf(false)
    var passwordHidden by mutableStateOf(true)
    var mCookie by mutableStateOf("")
    var userName by mutableStateOf("")
    var passWord by mutableStateOf("")
    private val client by lazy {
        OkHttpClient.Builder()
            .followRedirects(false)
            .build()
    }

    private fun context() = getApplication<Application>().applicationContext
    fun changePasswordVisual() {
        passwordHidden = !passwordHidden
    }

    private var session = ""
    private fun buildRequest(url: String, cookie: String = "") =
        Request.Builder()
            .url(url)
            .header("cookie", cookie)
            .build()

    private fun formBody() = FormBody.Builder()
        .add("username", userName)
        .add("password", passWord)
        .add("currentMenu", "1")
        .add("failN", "-1")
        .add("mfaState", "")
        .add("execution", "e1s1")
        .add("_eventId", "submit")
        .add("geolocation", "")
        .add("submit", "Login1")
        .build()

    private fun postRequest() = Request.Builder()
        .url("https://cas.paas.jmu.edu.cn/cas/login?service=http%3A%2F%2Fjwxt.jmu.edu.cn%2Fstudent%2Fsso%2Flogin")
        .header("cookie", session)
        .header("Host", "cas.paas.jmu.edu.cn")
        .post(formBody())
        .build()


    private fun toast(msg: String) {
        Toast.makeText(context(), msg, Toast.LENGTH_SHORT).show()
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterial3Api
    @ExperimentalFoundationApi
    suspend fun login(): Boolean {
        var localCookie = ""

        //User Password 格式基本判断
        if (userName.isBlank() || passWord.isBlank() || userName.length < 10) {
            toast(context().getString(R.string.makesure_input_correct))
            return false
        }

        try {
            //获取SESSION
            val sessionRsp = client.newCall(buildRequest(BASE_URL_LOGIN)).awaitResponse()
            sessionRsp.header("set-cookie")?.let {
                session = it.substring(it.indexOf("SESSION="), it.indexOf(";"))
            }
        } catch (e: IOException) {
            //网络错误
            Log.d(TAG, e.message!!)
            toast(context().getString(R.string.network_error))
            return false
        }

        client.newCall(postRequest()).awaitResponse().header("location")?.let {
            client.newCall(buildRequest(it)).awaitResponse().headers("set-cookie")
                .forEach { cookie ->
                    localCookie += cookie.substring(0, cookie.indexOf(";") + 1)
                }
            //获取Cookie
            localCookie = localCookie.removeSuffix(";")
        }

        val lastRsp =
            client.newCall(buildRequest("http://jwxt.jmu.edu.cn/student/sso/login", localCookie))
                .awaitResponse()
        val login = lastRsp.header("location").toString() == FLAG_URL

        //登录状态判断
        if (!login) toast(context().getString(R.string.login_fail)) else {
            mCookie = localCookie
        }
        return login
    }

    fun clearUser() { userName = "" }
}