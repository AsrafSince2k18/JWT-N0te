package com.since.jwt_note.domain.repo

import com.since.jwt_note.data.modal.RefreshTokenModal
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepo : MongoRepository<RefreshTokenModal, ObjectId> {

    fun findByUserIdAndRefreshToken(userId: ObjectId,token:String) : RefreshTokenModal?
    fun deleteByUserIdAndRefreshToken(userId: ObjectId,token:String) : Long

}