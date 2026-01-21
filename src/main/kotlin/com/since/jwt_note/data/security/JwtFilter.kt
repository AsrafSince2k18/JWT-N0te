package com.since.jwt_note.data.security

import com.since.jwt_note.domain.repo.UserRepo
import com.since.jwt_note.presentaction.exception.AccessTokenException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtFilter(
    @Value("\${API_KEY}") private val key: String,
    private val userRepo: UserRepo,
    private val jwtService: JwtService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val getApiKey = request.getHeader("x-api-key")
            if (getApiKey == null || getApiKey != key) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Invalid API_KEY")
                return
            }else{
                filterChain.doFilter(request,response)
            }

            val getToken = request.getHeader(HttpHeaders.AUTHORIZATION)

            if (getToken == null || !getToken.startsWith("Bearer ")) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Invalid Token")
                return
            }

            if (!jwtService.validateAccessToken(getToken)) {
                throw AccessTokenException("Token expiry or invalid")
            }
            val userId = jwtService.getUserIdFromToken(token = getToken)
            val user = userRepo.findById(ObjectId(userId))
                .orElseThrow {
                    throw AccessTokenException("Token expiry or invalid")
                }
            println(user)
            val securityContext = UsernamePasswordAuthenticationToken(user, null, emptyList())
            SecurityContextHolder.getContext().authentication = securityContext

            filterChain.doFilter(request, response)


        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.message)
        }
    }
}