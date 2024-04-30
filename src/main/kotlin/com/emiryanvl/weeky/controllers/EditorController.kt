package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
@RequestMapping("/editor")
class EditorController(private val restClient: RestClient) {
    @PostMapping("/save")
    fun saveArticle(
        @RequestParam link: String,
        @RequestParam content: String,
        @RequestParam title: String
    ): String {
        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article$link")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        currentArticle?.content = content
        currentArticle?.title = title

        currentArticle?.let {
            restClient.put()
                .uri("http://localhost:8081/article/${it.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .body(it)
                .retrieve()
                .toBodilessEntity()
        }

        return "redirect:$link"
    }
}