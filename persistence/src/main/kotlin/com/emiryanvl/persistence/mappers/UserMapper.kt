package com.emiryanvl.persistence.mappers

import com.emiryanvl.persistence.dto.requests.UserRequest
import com.emiryanvl.persistence.dto.responses.UserResponse
import com.emiryanvl.persistence.entities.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toUserEntity(userRequest: UserRequest): UserEntity {
        return UserEntity(
            userRequest.username,
            userRequest.password,
            userRequest.role
        )
    }

    fun toUserResponse(userEntity: UserEntity): UserResponse {
        return UserResponse(
            userEntity.id,
            userEntity.username
        )
    }
}