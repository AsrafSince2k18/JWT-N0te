package com.since.jwt_note.presentaction.exception.global_error.modal

import com.since.jwt_note.presentaction.mapper.timeFormat

data class ErrorModel(

    val time:String = System.currentTimeMillis().timeFormat(),
    val statusCode:Int,
    val statusMsg:String,
    val error:String?,
    val pathUrl:String,
    val url:String

)
