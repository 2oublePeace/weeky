package com.emiryanvl.webapp.dto

data class UserRequest(
    var username: String = "",
    var password: String = "",
    var role: String = "USER"
)
