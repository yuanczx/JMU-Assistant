package com.jmu.assistant.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.dataStore
import com.jmu.assistant.models.DataStoreObject.COOKIE_KEY
import com.jmu.assistant.utils.awaitResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class LoginViewModel(application: Application) : BaseViewModel(application) {
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
    private var mCookie by mutableStateOf("")
    var userName by mutableStateOf("")
    var passWord by mutableStateOf("")
    private val client by lazy {
        OkHttpClient.Builder()
            .followRedirects(false)
            .build()
    }

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


    @ExperimentalAnimationApi
    @ExperimentalMaterial3Api
    @ExperimentalFoundationApi
    fun login(afterLogin:suspend ()->Unit) {
        viewModelScope.launch {
            var localCookie = ""

            //User Password 格式基本判断
            if (userName.isBlank() || passWord.isBlank() || userName.length < 10) {
                toast(R.string.makesure_input_correct)
                cancel()
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
                toast(R.string.network_error)
                cancel()
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
                client.newCall(
                    buildRequest(
                        "http://jwxt.jmu.edu.cn/student/sso/login",
                        localCookie
                    )
                ).awaitResponse()
            val login = lastRsp.header("location").toString() == FLAG_URL

            //登录状态判断
            if (!login) toast(R.string.login_fail) else {
                mCookie = localCookie
                MainActivity.cookie = mCookie
                updateCookie()
                afterLogin()
            }
        }
    }

    fun clearUser() {
        userName = ""
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterial3Api
    @ExperimentalFoundationApi
    suspend fun updateCookie() {
        context().dataStore.edit {
            it[COOKIE_KEY] = mCookie
            if (rememberPwd) {
                it[userNameKey] = userName
                it[passWordKey] = passWord
            } else {
                it[passWordKey] = ""
                it[userNameKey] = ""
            }
        }
    }
}