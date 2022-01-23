package com.yuan.transcript

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yuan.transcript.entity.ContentNav
import com.yuan.transcript.ui.screen.CourseScreen
import com.yuan.transcript.ui.screen.HomeScreen
import com.yuan.transcript.ui.theme.TranscriptTheme
import com.yuan.transcript.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            Log.d("ViewModel", viewModel.toString())

            val navController = rememberNavController()
            TranscriptTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = ContentNav.Home.route
                    ) {
                        composable(ContentNav.Home.route) {
                            AnimatedVisibility(
                                visible = !viewModel.login,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AndroidView(factory = {
                                    WebView(it).apply {
                                        settings.javaScriptEnabled = true
                                        settings.useWideViewPort = true
                                        settings.loadWithOverviewMode = true
                                        settings.blockNetworkImage = false
                                        settings.domStorageEnabled = true
                                        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                                        settings.mixedContentMode =
                                            WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                        webViewClient = object : WebViewClient() {
                                            override fun shouldOverrideUrlLoading(
                                                view: WebView?,
                                                request: WebResourceRequest?
                                            ): Boolean {
                                                viewModel.url = request!!.url.toString()
                                                return true
                                            }
                                        }

                                    }
                                }, modifier = Modifier.height(400.dp)) {
                                    it.loadUrl(viewModel.url)
                                    if (viewModel.url == "http://jwxt.jmu.edu.cn/student/home") {
                                        viewModel.cookie =
                                            CookieManager.getInstance().getCookie(viewModel.url)
                                        Log.d("cookies", viewModel.cookie)
                                        viewModel.login = true
                                    }
                                }

                            }
                            if (viewModel.login){HomeScreen(viewModel.cookie,navController)}
                        }
                        composable(ContentNav.Detail.route) {
                            Text(text = "Hello")
                        }
                        composable(ContentNav.Course.route){
                            CourseScreen(navController,viewModel.cookie)
                        }
                    }
                }
            }
        }
    }

}
