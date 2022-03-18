package com.jmu.assistant.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jmu.assistant.utils.page.MesseagePageSource
import com.jmu.assistant.utils.page.MsgType

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class MesseageViewModel : ViewModel() {


    val msgList = listOf("学校新闻", "学校通知", "教务新闻", "教务通知")
    var selectedTabIndex by mutableStateOf(0)

    private val schoolNewsData = Pager(PagingConfig(pageSize = 10)) {
        MesseagePageSource(MsgType.SchoolNews)
    }.flow.cachedIn(viewModelScope)

    private val schoolNoteData = Pager(PagingConfig(pageSize = 10)) {
        MesseagePageSource(MsgType.SchoolNote)
    }.flow.cachedIn(viewModelScope)

    private val senateNewsData = Pager(PagingConfig(pageSize = 10)) {
        MesseagePageSource(MsgType.SenateNews)
    }.flow.cachedIn(viewModelScope)

    private val senateNoteData = Pager(PagingConfig(pageSize = 10)) {
        MesseagePageSource(MsgType.SenateNote)
    }.flow.cachedIn(viewModelScope)



    fun getData() = when (selectedTabIndex) {
        0 -> schoolNewsData
        1 -> schoolNoteData
        2 -> senateNewsData
        else -> senateNoteData
    }
}