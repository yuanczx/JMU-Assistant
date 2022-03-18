package com.jmu.assistant.utils.page

sealed class MsgType(val url:String) {
    object SchoolNews:MsgType("https://www.jmu.edu.cn/jdyw/jdxw")
    object SchoolNote:MsgType("https://www.jmu.edu.cn/tzgg/tzgg")
    object SenateNews:MsgType("https://jwc.jmu.edu.cn/index/jwxw")
    object SenateNote:MsgType("https://jwc.jmu.edu.cn/tztg")
}