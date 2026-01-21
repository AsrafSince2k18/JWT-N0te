package com.since.jwt_note.presentaction.exception

class AccessTokenException (msg:String): RuntimeException(msg)
class RefreshTokenException (msg:String): RuntimeException(msg)
class PasswordException (msg:String): RuntimeException(msg)
class EmailException (msg:String): RuntimeException(msg)
class EmailExistsException (msg:String): RuntimeException(msg)