package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.http.MediaType
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@RestController
class SearchController(private val restClient: RestClient) {
    @PostMapping("/{username}/search")
    fun searchArticle(@PathVariable username: String, @RequestParam searchText: String, model: Model): List<ArticleDto> {
        return restClient.get()
            .uri("http://localhost:8081/article/search?searchText=$searchText&username=$username")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>() ?: emptyList()
    }
}