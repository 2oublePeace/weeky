package com.emiryanvl.persistence.services

import com.emiryanvl.persistence.dto.requests.UserRequest
import com.emiryanvl.persistence.dto.responses.UserResponse

interface UserService {
    fun getUser(id: Long): UserResponse
    fun getAllUsers(): List<UserResponse>
    fun createUser(userRequest: UserRequest): UserResponse
    fun updateUser(id: Long, userRequest: UserRequest) : UserResponse
    fun deleteUser(id: Long)
}