package com.emiryanvl.persistence.dto.responses

data class UserResponse(
        val id: Long? = null,
        val username: String,
        val password: String,
        val role: String
)