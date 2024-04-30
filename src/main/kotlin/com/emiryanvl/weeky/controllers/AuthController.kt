package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.UserDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestClient

@Controller
@RequestMapping
class AuthController(private val restClient: RestClient, private val passwordEncoder: PasswordEncoder) {
    @Value("\${api.user}")
    private lateinit var userApi: String
    @GetMapping("/signin")
    fun getSignIn(): String = "signin"

    @GetMapping("/signup")
    fun getSignUp(): String = "signup"

    @PostMapping("/signup")
    fun registerUser(@RequestParam username: String, @RequestParam password: String): String {
        restClient.post()
            .uri(userApi)
            .contentType(MediaType.APPLICATION_JSON)
            .body(UserDto(username, passwordEncoder.encode(password)))
            .retrieve()
            .toBodilessEntity()

        return "redirect:/signin"
    }
}