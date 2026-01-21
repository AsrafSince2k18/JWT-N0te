package com.since.jwt_note.domain.repo

import com.since.jwt_note.data.modal.Notes
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepo : MongoRepository<Notes, ObjectId>{

    fun findByUserId(user: ObjectId): List<Notes>

}