package com.since.jwt_note.data.security

import com.since.jwt_note.presentaction.exception.AccessTokenException
import com.since.jwt_note.presentaction.exception.RefreshTokenException
import io.jsonwebtoken.ClaimJwtException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date


@Service
class JwtService (
    @Value("\${SECRET_KEY}") private val key:String

){

    private val secretKey = Keys.hmacShaKeyFor(
        Base64.getDecoder().decode(key)
    )


    companion object{
        private const val ACCESS_TOKEN=6L*60L*1000L
        const val REFRESH_TOKEN=10L*60L*1000L

        private const val ACCESS_TYPE = "ACCESS"
        private const val REFRESH_TYPE = "REFRESH"
    }


    private fun generateToken(
        userId:String,
        type:String,
        time:Long
    ):String{
        val date = Date()
        val expiryDate = Date(date.time+time)

        return Jwts.builder()
            .header()
            .type("JWT")
            .and()
            .subject(userId)
            .issuedAt(date)
            .expiration(expiryDate)
            .claim("type",type)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }



    fun generateAccessToken(userId: String):String{
        return generateToken(userId = userId, type = ACCESS_TYPE,time=ACCESS_TOKEN)
    }

    fun generateRefreshToken(userId: String):String{
        return generateToken(userId = userId, type = REFRESH_TYPE,time=REFRESH_TOKEN)
    }


    private fun validateToken(token:String): Claims?{
       return try {
            val rawToken = if(token.startsWith("Bearer ")){
                token.removePrefix("Bearer ")
            }else{
                token
            }
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        }catch (e: ClaimJwtException){
            throw AccessTokenException(e.message.toString())
        }catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }


    fun validateAccessToken(token: String) : Boolean{
       return try {
           val validToken = validateToken(token) ?: return false
           val type = validToken["type"] as String
           type == ACCESS_TYPE
       }catch (e: ClaimJwtException){
           e.printStackTrace()
           throw AccessTokenException(msg=e.message.toString())
       }catch (e: JwtException){
           e.printStackTrace()
           throw AccessTokenException(msg=e.message.toString())
       }
    }

    fun validateRefreshToken(token: String) : Boolean{
        return try {
            val validToken = validateToken(token) ?: return false
            val type = validToken["type"] as String
            type == REFRESH_TYPE
        }catch (e: ClaimJwtException){
            e.printStackTrace()
            throw RefreshTokenException(msg=e.message.toString())
        }catch (e: ClaimJwtException){
            e.printStackTrace()
            throw AccessTokenException(msg=e.message.toString())
        }
    }

    fun getUserIdFromToken(token: String):String?{
        val validToken = validateToken(token=token) ?: return null
        return validToken.subject
    }

}