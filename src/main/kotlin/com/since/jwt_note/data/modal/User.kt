package com.since.jwt_note.data.modal

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("user")
data class User(

    @Id
    val userId: ObjectId = ObjectId.get(),
    val email:String,
    val password: String

)
