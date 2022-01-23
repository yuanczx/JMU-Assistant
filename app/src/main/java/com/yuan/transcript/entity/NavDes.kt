package com.yuan.transcript.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.res.stringResource
import com.yuan.transcript.R


sealed class ContentNav(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val drawableId: Int? = null
) {
    object Home : ContentNav("Home", R.string.Home)
    object Detail : ContentNav("Detail", R.string.Detail)
    object Course : ContentNav("Course",R.string.Course)
}

sealed class BtmNav(val route: String, @StringRes val stringId: Int,@DrawableRes val drawableId: Int?=null) {
    object Func : BtmNav("Func", R.string.func,R.drawable.ic_func)
    object User : BtmNav("User", R.string.user,R.drawable.ic_user)
}
