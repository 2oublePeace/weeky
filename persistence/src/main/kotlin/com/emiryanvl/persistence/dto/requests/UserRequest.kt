package com.emiryanvl.persistence.dto.requests

data class UserRequest (
    val username: String,
    val password: String,
    val role: String,
)