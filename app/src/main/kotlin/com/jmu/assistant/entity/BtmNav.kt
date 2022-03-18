package com.jmu.assistant.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jmu.assistant.R

sealed class BtmNav(val route: String, @StringRes val stringId: Int, @DrawableRes val drawableId: Int?=null) {
    object Func : BtmNav("Func", R.string.Func, R.drawable.ic_func)
    object User : BtmNav("User", R.string.User, R.drawable.ic_user)
    object News : BtmNav("News", R.string.News,R.drawable.ic_baseline_newspaper_24)
}
