package com.since.jwt_note.data.modal

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document("note")
data class Notes(

    @Id
    val id: ObjectId = ObjectId.get(),
    val userId: ObjectId,
    val title:String,
    val content:String,
    val time:Long= System.currentTimeMillis()

)
