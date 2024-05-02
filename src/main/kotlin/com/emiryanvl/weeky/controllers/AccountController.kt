package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.UserDto
import com.emiryanvl.weeky.dto.UserRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
class AccountController(private val restClient: RestClient, private val passwordEncoder: PasswordEncoder) {
    @Value("\${api.user}")
    private lateinit var userApi: String

    @GetMapping("/{username}/account")
    fun getAccount(@PathVariable username: String): String {
        return "account"
    }

    @GetMapping("/{username}/account/change-password")
    fun changePassword(
        @PathVariable username: String,
        @RequestParam oldPassword: String,
        @RequestParam newPassword: String,
        @RequestParam confirmNewPassword: String,
    ) : String {
        val user = restClient.get()
            .uri("$userApi/$username")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<UserDto>()

        return user?.let {
            if(passwordEncoder.matches(oldPassword, user.password) && newPassword == confirmNewPassword) {
                restClient.put()
                    .uri("$userApi/${it.id}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(UserRequest(it.username, passwordEncoder.encode(newPassword)))
                    .retrieve()
                    .toBodilessEntity()
                "redirect:/logout"
            } else "error"
        } ?: "error"
    }
}