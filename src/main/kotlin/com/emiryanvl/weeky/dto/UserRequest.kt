package com.emiryanvl.weeky.dto

data class UserRequest(
    var username: String = "",
    var password: String = "",
    var role: String = "USER"
)
