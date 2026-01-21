package com.since.jwt_note.presentaction.mapper

import com.since.jwt_note.data.modal.Notes
import com.since.jwt_note.presentaction.controller.NoteController
import java.text.SimpleDateFormat
import java.util.Date

fun Notes.toNoteResponse(): NoteController.NoteResponse{
    return NoteController.NoteResponse(
        id=id.toHexString(),
        userId = userId.toHexString(),
        title=title,
        content = content,
        time = time.timeFormat()
    )
}



fun Long.timeFormat():String{
    val sdf = SimpleDateFormat()
    val date = Date(this)
    return sdf.format(date)
}