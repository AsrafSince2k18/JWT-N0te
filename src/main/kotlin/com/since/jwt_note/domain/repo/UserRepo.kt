package com.since.jwt_note.domain.repo

import com.since.jwt_note.data.modal.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepo : MongoRepository<User, ObjectId>{

    fun findByEmail(email:String) : User?

}