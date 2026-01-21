package com.since.jwt_note.data.modal

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date


@Document("refreshTokenModal")
data class RefreshTokenModal(

    val userId : ObjectId,
    val refreshToken:String,
    val issuesAt:Long,
    @Indexed(expireAfterSeconds = 0)
    val expiryAt : Date

)
