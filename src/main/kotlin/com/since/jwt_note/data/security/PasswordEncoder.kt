package com.since.jwt_note.data.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
class PasswordEncoder  {

    private val passwordEncoder = BCryptPasswordEncoder()

    val encodePassword :(String) ->String={encode->
        passwordEncoder.encode(encode)
    }

    val decodePassword  :(String,String) -> Boolean={rawPassword,encodePassword->
        passwordEncoder.matches(rawPassword,encodePassword)
    }

}