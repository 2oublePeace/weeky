package com.emiryanvl.webapp.controllers

import com.emiryanvl.webapp.dto.ArticleDto
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${api.article}")
    private lateinit var articleApi: String

    @PostMapping("/{username}/search")
    fun searchArticle(@PathVariable username: String, @RequestParam searchText: String, model: Model): List<ArticleDto> {
        return restClient.get()
            .uri("$articleApi/search?searchText=$searchText&username=$username")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>() ?: emptyList()
    }
}