package com.emiryanvl.webapp.dto

data class UserDto (
    var id: Long?  = null,
    var username: String = "",
    var password: String = "",
    var role: String = "USER"
)