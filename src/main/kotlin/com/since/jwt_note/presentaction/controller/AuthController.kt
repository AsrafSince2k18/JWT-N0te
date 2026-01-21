package com.since.jwt_note.presentaction.controller

import com.since.jwt_note.data.modal.User
import com.since.jwt_note.data.security.AuthService
import com.since.jwt_note.presentaction.mapper.toTokenResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController (
    private val authService: AuthService
){

    data class AuthRequest(
        @field:NotBlank(message = "Enter the email")
        val email: String,
        @field:Length(min = 8, max = 20, message = "Password length to sort, minimum length characters")
        val password:String
    )

    data class AuthResponse(
        val accessToken:String,
        val refreshToken:String
    )

    data class TokenRequest(
        @field:NotBlank(message = "Enter the token")
        val refreshToken: String
    )


    @PostMapping("/signup")
    fun signUp(
        @RequestBody @Valid body: AuthRequest
    ): ResponseEntity<User>{
        val signUpUser = authService.signUp(email = body.email, password = body.password)
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpUser)
    }


    @PostMapping("/signin")
    fun signIn(
        @RequestBody @Valid body: AuthRequest
    ): ResponseEntity<AuthResponse>{

        val signInUser = authService.signIn(
            email = body.email,
            password = body.password
        )
        return ResponseEntity.status(HttpStatus.OK).body(signInUser.toTokenResponse())
    }



    @PostMapping("/refresh")
    fun refresh(
        @RequestBody @Valid body: TokenRequest
    ): ResponseEntity<AuthResponse>{
        val token = authService.refreshToken(token = body.refreshToken)
        return ResponseEntity.status(HttpStatus.OK).body(token.toTokenResponse())
    }

}