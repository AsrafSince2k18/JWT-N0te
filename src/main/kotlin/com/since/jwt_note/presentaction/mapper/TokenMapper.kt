package com.since.jwt_note.presentaction.mapper

import com.since.jwt_note.data.security.AuthService
import com.since.jwt_note.presentaction.controller.AuthController

fun AuthService.TokenResponse.toTokenResponse() : AuthController.AuthResponse {
    return AuthController.AuthResponse(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}