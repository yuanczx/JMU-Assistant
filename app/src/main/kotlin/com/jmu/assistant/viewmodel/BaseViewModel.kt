package com.jmu.assistant.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel

open class BaseViewModel(application: Application):AndroidViewModel(application) {
    fun context() = getApplication<Application>().applicationContext
    fun toast(msg:String){
        Toast.makeText(context(), msg, Toast.LENGTH_SHORT).show()
    }

    fun toast(@StringRes id:Int){
        toast(context().getString(id))
    }
}