package com.jmu.assistant.ui.screen

import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmu.assistant.viewmodel.UserViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserScreen(mainNavController: NavHostController) {
    val viewModel: UserViewModel = viewModel()
    Log.d("ViewModel", viewModel.toString())
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Hello world")
    }
    }