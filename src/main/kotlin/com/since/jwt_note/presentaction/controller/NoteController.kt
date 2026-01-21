package com.since.jwt_note.presentaction.controller

import com.since.jwt_note.data.modal.Notes
import com.since.jwt_note.data.modal.User
import com.since.jwt_note.domain.repo.NoteRepo
import com.since.jwt_note.presentaction.exception.EmptyListException
import com.since.jwt_note.presentaction.exception.InvalidException
import com.since.jwt_note.presentaction.exception.NotFoundException
import com.since.jwt_note.presentaction.mapper.toNoteResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.hibernate.validator.constraints.Length
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RequestMapping("/notes")
@RestController
class NoteController {

    @Autowired
    private lateinit var noteRepo: NoteRepo

    fun setterInjection(noteRepo: NoteRepo){
        this.noteRepo=noteRepo
    }



    data class NoteResponse(
        val id:String,
        val userId:String,
        val title:String,
        val content:String,
        val time: String
    )


    data class NoteRequest(
        @field:NotBlank(message = "Must enter Title")
        val title: String,
        @field:Length(min = 3, max = 1000, message = "Minimum length charters 3")
        val content:String
    )


    @GetMapping
    fun getAllUserNote(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<List<NoteResponse>>{
        val allNote = noteRepo.findByUserId(user.userId)
        return if(allNote.isNotEmpty()){
            val note=allNote
                .map {
                    it.toNoteResponse()
                }
            ResponseEntity.status(HttpStatus.OK).body(note)
        }else{
            throw EmptyListException("Note is empty, Add new note")
        }
    }


    @GetMapping("/{id}")
    fun getNote(
        @PathVariable id :String
    ): ResponseEntity<NoteResponse>{

        if(!ObjectId.isValid(id)){
            throw InvalidException(msg = "Invalid id= $id")
        }

        val findNote = noteRepo.findById(ObjectId(id))
            .orElseThrow {
                throw NotFoundException(msg = "This id not found")
            }
        return ResponseEntity.status(HttpStatus.OK).body(findNote.toNoteResponse())
    }



    @PostMapping
    fun addNote(
        @AuthenticationPrincipal user: User,
        @RequestBody @Valid body: NoteRequest
    ): ResponseEntity<NoteResponse>{
        val note = Notes(
            title = body.title,
            content = body.content,
            userId = user.userId
        )
        val saveNote = noteRepo.save<Notes>(note)
        return ResponseEntity.status(HttpStatus.CREATED).body(saveNote.toNoteResponse())
    }


    @PutMapping("/{id}")
    fun updateNote(
        @AuthenticationPrincipal user: User,
        @PathVariable id:String,
        @RequestBody @Valid body: NoteRequest
    ): ResponseEntity<NoteResponse>{
        if(!ObjectId.isValid(id)){
            throw InvalidException(msg = "Invalid id= $id")
        }

        val findNote = noteRepo.findById(ObjectId(id))
            .orElseThrow {
                throw NotFoundException(msg = "This id not found")
            }
       return if(user.userId == findNote.userId) {
            val note = findNote.copy(
                title = body.title,
                content = body.content
            )
            val updateNote = noteRepo.save<Notes>(note)
            ResponseEntity.status(HttpStatus.OK).body(updateNote.toNoteResponse())
        }else{
            throw NotFoundException(msg = "This id not found")
        }
    }


    @DeleteMapping("/{id}")
    fun deleteNote(
        @PathVariable id :String
    ): ResponseEntity<Unit>{
        if(!ObjectId.isValid(id)){
            throw InvalidException(msg = "Invalid id= $id")
        }

        val findNote = noteRepo.findById(ObjectId(id))
            .orElseThrow {
                throw NotFoundException(msg = "This id not found")
            }
        noteRepo.delete(findNote)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping
    fun deleteAllNote(
        @AuthenticationPrincipal user: User

    ): ResponseEntity<Unit>{

        val allItem = noteRepo.findByUserId(user.userId)
        if(allItem.isEmpty()){
            throw EmptyListException(msg = "Note is empty, Add new note")
        }

        noteRepo.deleteAll(allItem)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


}