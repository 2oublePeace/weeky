package com.emiryanvl.persistence.integrated

import com.emiryanvl.persistence.dto.requests.UserRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun createUserTest() {
        val request = UserRequest("user", "password", "USER")

        mockMvc.perform(
            post("/user")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isCreated)
    }

    @Test
    fun getUserTest() {
        var id: Long? = null
        val request = UserRequest("user", "password", "USER")

        mockMvc.perform(
            post("/user")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andDo {
                id = ObjectMapper().readTree(it.response.contentAsString).get("id").asLong()
            }

        mockMvc.perform(
            get("/user/${request.username}")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
    }

    @Test
    fun updateUserTest() {
        var id: Long? = null
        val createRequest = UserRequest("user", "password", "USER")
        val updateRequest = UserRequest("updatedUser", "updatedPassword", "USER")

        mockMvc.perform(
            post("/user")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createRequest))
        )
            .andExpect(status().isCreated)
            .andDo {
                id = ObjectMapper().readTree(it.response.contentAsString).get("id").asLong()
            }

        mockMvc.perform(
            put("/user/$id")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value(updateRequest.username))
            .andExpect(jsonPath("$.password").value(updateRequest.password))
            .andExpect(jsonPath("$.role").value(updateRequest.role))
    }

    @Test
    fun deleteUserTest() {
        var id: Long? = null
        val request = UserRequest("user", "password", "USER")

        mockMvc.perform(
            post("/user")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andDo {
                id = ObjectMapper().readTree(it.response.contentAsString).get("id").asLong()
            }

        mockMvc.perform(
            delete("/user/$id")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent)
    }
}