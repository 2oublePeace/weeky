package com.emiryanvl.persistence.integrated

import com.emiryanvl.persistence.dto.requests.ArticleRequest
import com.emiryanvl.persistence.dto.requests.UserRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.sql.Date
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ArticleControllerIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun createHomeArticle() {
        val request = UserRequest("user", "password", "USER")

        mockMvc.perform(
            post("/user")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isCreated)
    }

    @Test
    fun createArticleTest() {

        val request = ArticleRequest(
            "Футбол",
            "/user/home/football",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )

        mockMvc.perform(
            post("/article")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isCreated)
    }

    @Test
    fun getArticleTest() {
        var id: Long? = null
        val request = ArticleRequest(
            "Футбол",
            "/user/home/football",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )

        mockMvc.perform(
            post("/article")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andDo {
                id = ObjectMapper().readTree(it.response.contentAsString).get("id").asLong()
            }

        mockMvc.perform(
            get("/article/${request.link}")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
    }

    @Test
    fun updateArticleTest() {
        var id: Long? = null
        val createRequest = ArticleRequest(
            "Футбол",
            "/user/home/soccer",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )
        val updateRequest = ArticleRequest(
            "Хоккей",
            "/user/home/hockey",
            "/user/home",
            "**Хоккейный контент**",
            Date.valueOf(LocalDate.now())
        )

        mockMvc.perform(
            post("/article")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createRequest))
        )
            .andExpect(status().isCreated)
            .andDo {
                id = ObjectMapper().readTree(it.response.contentAsString).get("id").asLong()
            }

        mockMvc.perform(
            put("/article/$id")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value(updateRequest.title))
            .andExpect(jsonPath("$.link").value(updateRequest.link))
            .andExpect(jsonPath("$.content").value(updateRequest.content))
            .andExpect(jsonPath("$.parentLink").value(updateRequest.parentLink))
            .andExpect(jsonPath("$.date").value(updateRequest.date.toString()))
    }

    @Test
    fun deleteArticleTest() {
        var id: Long? = null
        val request = ArticleRequest(
            "Футбол",
            "/user/home/football",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )

        mockMvc.perform(
            post("/article")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andDo {
                id = ObjectMapper().readTree(it.response.contentAsString).get("id").asLong()
            }

        mockMvc.perform(
            delete("/article/$id")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent)
    }
}