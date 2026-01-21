package com.since.jwt_note.data.security

import com.since.jwt_note.data.modal.RefreshTokenModal
import com.since.jwt_note.data.modal.User
import com.since.jwt_note.data.security.JwtService.Companion.REFRESH_TOKEN
import com.since.jwt_note.domain.repo.RefreshTokenRepo
import com.since.jwt_note.domain.repo.UserRepo
import com.since.jwt_note.presentaction.exception.EmailException
import com.since.jwt_note.presentaction.exception.EmailExistsException
import com.since.jwt_note.presentaction.exception.PasswordException
import com.since.jwt_note.presentaction.exception.RefreshTokenException
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.*

@Service
class AuthService(
    private val userRepo: UserRepo,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenRepo: RefreshTokenRepo
) {

    data class TokenResponse(
        val accessToken:String,
        val refreshToken:String
    )

    fun signUp(email:String,
               password:String): User{

        val findEmail = userRepo.findByEmail(email=email.trim())
        return if(findEmail==null){
            val user = User(
                email=email.trim(),
                password = passwordEncoder.encodePassword(password)
            )
             userRepo.save<User>(user)
        }else{
            throw EmailExistsException(msg = "Already exists email")
        }


    }


    fun signIn(
        email:String,
        password:String
    ): TokenResponse{

        val user = userRepo.findByEmail(email = email)
            ?: throw EmailException("User not found, enter correct valid email")

        val validPassword = passwordEncoder.decodePassword(password,user.password)
        if(!validPassword){
            throw PasswordException(msg="Incorrect password")
        }

        val generateAccessToken = jwtService.generateAccessToken(userId = user.userId.toHexString())
        val generateRefreshToken = jwtService.generateRefreshToken(userId = user.userId.toHexString())
        storeRefreshToken(
            token = generateRefreshToken
        )
        return TokenResponse(
            accessToken = generateAccessToken,
            refreshToken = generateRefreshToken
        )
    }


    fun refreshToken(token: String): TokenResponse{

        if(!jwtService.validateRefreshToken(token = token)){
            throw RefreshTokenException("Invalid token or expiry")
        }

        val userId = jwtService.getUserIdFromToken(token=token) ?:
                        throw RefreshTokenException("Invalid token or expiry")

        val findUser = userRepo.findById(ObjectId(userId))
            .orElseThrow {
                RefreshTokenException("Invalid token or expiry")
            }

        val encodeToken = encodeRefreshToken(token)

        val findRefreshToken = refreshTokenRepo.findByUserIdAndRefreshToken(findUser.userId, token = encodeToken)

        if(findRefreshToken!=null){
            refreshTokenRepo.deleteByUserIdAndRefreshToken(findUser.userId, token = encodeToken)
        }

        val newAccessToken = jwtService.generateAccessToken(userId = findUser.userId.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(userId = findUser.userId.toHexString())

        storeRefreshToken(token = newRefreshToken)
        return TokenResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )


    }


    private fun encodeRefreshToken(token:String):String{
        val digest = MessageDigest.getInstance("SHA-256")
        val byteArray = digest.digest(token.toByteArray())
        val encodeToken = Base64.getEncoder().encodeToString(byteArray)
        return encodeToken
    }


    private fun storeRefreshToken(token:String){

        val getUserId = jwtService.getUserIdFromToken(token=token)
        val encodeToken = encodeRefreshToken(token)
        val currentTime = Date().time
        val expiryTime = Date(currentTime + REFRESH_TOKEN)



        val refreshTokenModal = RefreshTokenModal(
            userId = ObjectId(getUserId),
            refreshToken = encodeToken,
            issuesAt = currentTime,
            expiryAt = expiryTime
        )
        refreshTokenRepo.save<RefreshTokenModal>(refreshTokenModal)
    }

}