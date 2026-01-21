package com.since.jwt_note.presentaction.exception.global_error

import com.since.jwt_note.presentaction.exception.AccessTokenException
import com.since.jwt_note.presentaction.exception.EmailException
import com.since.jwt_note.presentaction.exception.EmailExistsException
import com.since.jwt_note.presentaction.exception.EmptyListException
import com.since.jwt_note.presentaction.exception.InvalidException
import com.since.jwt_note.presentaction.exception.NotFoundException
import com.since.jwt_note.presentaction.exception.PasswordException
import com.since.jwt_note.presentaction.exception.RefreshTokenException
import com.since.jwt_note.presentaction.exception.global_error.modal.ErrorModel
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun fieldErrorException(
        servlet: HttpServletRequest,
        e: MethodArgumentNotValidException
    ): ResponseEntity<ErrorModel>{

        val error = e.fieldErrors
            .joinToString {
                "${it.field}:${it.defaultMessage}"
            }

        val errorModel = ErrorModel(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            statusMsg = HttpStatus.BAD_REQUEST.name,
            error = error,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel)
    }



    //todo noteException start
    @ExceptionHandler(EmptyListException::class)
    fun emptyNoteException(e: EmptyListException,
                           servlet: HttpServletRequest) : ResponseEntity<ErrorModel>{

        val errorModel = ErrorModel(
            statusCode = HttpStatus.OK.value(),
            statusMsg = HttpStatus.OK.name,
            error = e.message,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.OK).body(errorModel)
    }


    @ExceptionHandler(NotFoundException::class)
    fun noteNotFoundException(e: NotFoundException,
                           servlet: HttpServletRequest) : ResponseEntity<ErrorModel>{

        val errorModel = ErrorModel(
            statusCode = HttpStatus.NOT_FOUND.value(),
            statusMsg = HttpStatus.NOT_FOUND.name,
            error = e.message,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel)
    }

    @ExceptionHandler(InvalidException::class)
    fun invalidIdException(e: InvalidException,
                           servlet: HttpServletRequest) : ResponseEntity<ErrorModel>{

        val errorModel = ErrorModel(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            statusMsg = HttpStatus.BAD_REQUEST.name,
            error = e.message,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel)
    }

    //todo noteException end

    //todo startAuthException
    @ExceptionHandler(AccessTokenException::class)
    fun accessTokenException(e: AccessTokenException,
                           servlet: HttpServletRequest) : ResponseEntity<ErrorModel>{

        val errorModel = ErrorModel(
            statusCode = HttpStatus.UNAUTHORIZED.value(),
            statusMsg = HttpStatus.UNAUTHORIZED.name,
            error = e.message,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorModel)
    }


    @ExceptionHandler(RefreshTokenException::class)
    fun refreshTokenException(e: RefreshTokenException,
                              servlet: HttpServletRequest) : ResponseEntity<ErrorModel>{

        val errorModel = ErrorModel(
            statusCode = HttpStatus.UNAUTHORIZED.value(),
            statusMsg = HttpStatus.UNAUTHORIZED.name,
            error = e.message,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorModel)
    }

    @ExceptionHandler(PasswordException::class)
    fun passwordException(e: PasswordException,
                          servlet: HttpServletRequest) : ResponseEntity<ErrorModel>{

        val errorModel = ErrorModel(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            statusMsg = HttpStatus.BAD_REQUEST.name,
            error = e.message,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel)
    }



    @ExceptionHandler(EmailException::class)
    fun emailException(e: EmailException,
                          servlet: HttpServletRequest) : ResponseEntity<ErrorModel>{

        val errorModel = ErrorModel(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            statusMsg = HttpStatus.BAD_REQUEST.name,
            error = e.message,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel)
    }

    @ExceptionHandler(EmailExistsException::class)
    fun emailException(e: EmailExistsException,
                       servlet: HttpServletRequest) : ResponseEntity<ErrorModel>{

        val errorModel = ErrorModel(
            statusCode = HttpStatus.CONFLICT.value(),
            statusMsg = HttpStatus.CONFLICT.name,
            error = e.message,
            pathUrl = servlet.requestURI,
            url = servlet.requestURL.toString()
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorModel)
    }

    //todo endAuthException

}