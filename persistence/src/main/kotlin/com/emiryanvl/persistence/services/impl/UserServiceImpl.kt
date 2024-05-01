package com.emiryanvl.persistence.services.impl

import com.emiryanvl.persistence.dto.requests.UserRequest
import com.emiryanvl.persistence.dto.responses.UserResponse
import com.emiryanvl.persistence.entities.ArticleEntity
import com.emiryanvl.persistence.entities.UserEntity
import com.emiryanvl.persistence.exceptions.NotFoundException.Companion.notFoundException
import com.emiryanvl.persistence.mappers.UserMapper
import com.emiryanvl.persistence.repositories.ArticleRepository
import com.emiryanvl.persistence.repositories.UserRepository
import com.emiryanvl.persistence.services.UserService
import org.springframework.stereotype.Service
import java.sql.Date
import java.time.LocalDate

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val userMapper: UserMapper
) : UserService {
    override fun getUser(username: String): UserResponse {
        return userMapper.toUserResponse(
            userRepository.findByUsername(username).orElseThrow(
                notFoundException("Пользователь не найден")
            )
        )
    }
    
    override fun getAllUsers(): List<UserResponse> = userRepository.findAll().map { userMapper.toUserResponse(it) }

    override fun createUser(userRequest: UserRequest): UserResponse {
        return userMapper.toUserResponse(
            userRepository.save(
                complementUser(userMapper.toUserEntity(userRequest))
            )
        )
    }

    override fun updateUser(id: Long, userRequest: UserRequest): UserResponse {
        return userMapper.toUserResponse(
            userRepository.save(
                editUser(
                    userRepository.findById(id).orElseThrow(notFoundException("Пользователь не найден")),
                    userRequest
                )
            )
        )
    }

    override fun deleteUser(id: Long) = userRepository.deleteById(id)

    private fun complementUser(userEntity: UserEntity): UserEntity {
        return userEntity.apply {
            articleRepository.save(
                ArticleEntity(
                    "Главная страница",
                    "/${userEntity.username}/home",
                    "/${userEntity.username}",
                    "Это главная страница",
                    Date.valueOf(LocalDate.now()),
                )
            )
        }
    }

    private fun editUser(userEntity: UserEntity, userRequest: UserRequest): UserEntity {
        return userEntity.apply {
            userEntity.username = userRequest.username
            userEntity.password = userRequest.password
        }
    }
}