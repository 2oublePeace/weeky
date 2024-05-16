package com.emiryanvl.persistence.unit

import com.emiryanvl.persistence.dto.requests.UserRequest
import com.emiryanvl.persistence.dto.responses.UserResponse
import com.emiryanvl.persistence.entities.ArticleEntity
import com.emiryanvl.persistence.entities.UserEntity
import com.emiryanvl.persistence.mappers.UserMapper
import com.emiryanvl.persistence.repositories.ArticleRepository
import com.emiryanvl.persistence.repositories.UserRepository
import com.emiryanvl.persistence.services.impl.UserServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Date
import java.time.LocalDate
import java.util.*

@SpringBootTest
class UserServiceImplTest {
    private val articleRepository = mockk<ArticleRepository>()
    private val userRepository = mockk<UserRepository>()
    private val userMapper = UserMapper()
    private val userService = UserServiceImpl(userRepository, articleRepository, userMapper)

    @Test
    fun getUserTest() {
        //Given
        val username = "user"
        val expected = UserResponse(
            1L,
            "user",
            "123",
            "USER"
        )
        val foundedUser = UserEntity(
            "user",
            "123" ,
            "USER"
        ).also { it.id = 1L }

        //When
        every { userRepository.findByUsername(username) } returns Optional.ofNullable(foundedUser)

        //Then
        val actual = userService.getUser(username)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun getAllUsersTest() {
        //Given
        val foundedUsers = mutableListOf(
            UserEntity(
                "user1",
                "123",
                "USER"
            ).also { it.id = 1L },
            UserEntity(
                "user2",
                "456",
                "USER"
            ).also { it.id = 2L }
        )
        val expected = mutableListOf(
            UserResponse(
                1L,
                "user1",
                "123",
                "USER"
            ),
            UserResponse(
                2L,
                "user2",
                "456",
                "USER"
            )
        )
        //When
        every { userRepository.findAll() } returns foundedUsers

        //Then
        val actual = userService.getAllUsers()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun createUserTest() {
        //Given
        val request = UserRequest(
            "user1",
            "123",
            "USER"
        )
        val homeArticleEntity = ArticleEntity(
            "Главная страница",
            "/user/home",
            "/user",
            "**Главный контент**",
            Date.valueOf(LocalDate.now())
        ).also { it.id = 1 }
        val expected = UserResponse(
            1L,
            "user1",
            "123",
            "USER"
        )

        //When
        every { userRepository.save(any()) } returns userMapper.toUserEntity(request).also {
            it.id = 1L
        }
        every { articleRepository.save(any()) } returns homeArticleEntity

        //Then
        val actual = userService.createUser(request)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun updateUserTest() {
        //Given
        val id = 1L
        val request = UserRequest(
            "user2",
            "456",
            "USER"
        )
        val expected = UserResponse(
            1L,
            "user2",
            "456",
            "USER"
        )
        val existUser = UserEntity(
            "user1",
            "123",
            "USER"
        ).also { it.id = 1L }

        //When
        every { userRepository.save(any()) } returns userMapper.toUserEntity(request).also {
            it.id = 1L
        }
        every { userRepository.findById(id) } returns Optional.ofNullable(existUser)

        //Then
        val actual = userService.updateUser(id, request)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun deleteUserTest() {
        //Given
        val id = 1L

        //When
        every { userRepository.deleteById(id) } returns Unit

        //Then
        userService.deleteUser(id)
        verify(exactly = 1) { userService.deleteUser(id) }
    }
}