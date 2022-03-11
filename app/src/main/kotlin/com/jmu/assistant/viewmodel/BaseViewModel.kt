package com.jmu.assistant.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel

open class BaseViewModel(application: Application):AndroidViewModel(application) {
    fun context(): Context = getApplication<Application>().applicationContext
    private fun toast(msg:String){
        Toast.makeText(context(), msg, Toast.LENGTH_SHORT).show()
    }

    fun toast(@StringRes id:Int){
        toast(getString(id))
    }

    fun getString(@StringRes id:Int) = context().getString(id)
}