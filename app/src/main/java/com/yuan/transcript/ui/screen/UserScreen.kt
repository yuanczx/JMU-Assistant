package com.yuan.transcript.ui.screen

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.yuan.transcript.viewmodel.UserViewModel

@Composable
fun UserScreen(mainNavController: NavHostController) {
    val viewModel:UserViewModel = viewModel()
    Log.d("ViewModel",viewModel.toString())
    Text(text = "Hello this is UserScreen")
}