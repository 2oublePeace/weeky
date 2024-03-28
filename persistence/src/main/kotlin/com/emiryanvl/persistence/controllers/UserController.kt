package com.emiryanvl.persistence.controllers

import com.emiryanvl.persistence.dto.requests.UserRequest
import com.emiryanvl.persistence.dto.responses.UserResponse
import com.emiryanvl.persistence.mappers.UserMapper
import com.emiryanvl.persistence.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {
    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String) : UserResponse {
        return userService.getUser(username)
    }

    @GetMapping
    fun getAllUsers() : ResponseEntity<List<UserResponse>> {
        return ResponseEntity<List<UserResponse>>(userService.getAllUsers(), HttpStatus.OK)
    }

    @PostMapping
    fun createUser(@RequestBody userRequest: UserRequest) : ResponseEntity<UserResponse> {
        return ResponseEntity<UserResponse>(userService.createUser(userRequest), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody userRequest: UserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity<UserResponse>(userService.updateUser(id, userRequest), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) : ResponseEntity<Unit> {
        return ResponseEntity<Unit>(userService.deleteUser(id), HttpStatus.NO_CONTENT)
    }
}