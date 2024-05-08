package com.emiryanvl.webapp.services

import com.emiryanvl.webapp.dto.UserDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class UserService(private val restClient: RestClient) {
    @Value("\${api.user}")
    private lateinit var userApi: String

    fun getUser(username: String): UserDto? {
        return restClient.get()
            .uri("$userApi/$username")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<UserDto>()
    }
}