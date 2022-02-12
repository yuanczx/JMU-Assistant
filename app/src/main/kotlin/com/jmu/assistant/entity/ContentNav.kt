package com.jmu.assistant.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jmu.assistant.R

sealed class ContentNav(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val drawableId: Int?=null
) {
    object Transcript : ContentNav("Transcript", R.string.Transcript, R.drawable.ic_transcript)
    object Course : ContentNav("Course", R.string.Course, R.drawable.ic_course)
    object Login : ContentNav("Login", R.string.Login)
}