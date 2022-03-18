package com.jmu.assistant.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jmu.assistant.R

sealed class ContentNav(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val drawableId: Int?=null
) {
    object Transcript : ContentNav("Transcript", R.string.Transcript, R.drawable.ic_score)
    object Course : ContentNav("Course", R.string.Course, R.drawable.ic_course)
    object Login : ContentNav("Login", R.string.Login)
    object Menu : ContentNav("Menu",R.string.menu,R.drawable.ic_func)
    object Info :ContentNav("Info",R.string.News)
//    object Train : ContentNav("Train",R.string.train_program,R.drawable.ic_train_program)
}