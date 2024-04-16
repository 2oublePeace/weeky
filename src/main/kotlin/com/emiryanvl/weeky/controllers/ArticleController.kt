package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.client.body


@Controller
@RequestMapping("/article")
class ArticleController(private val restClient: RestClient) {
    @PostMapping("/create")
    fun createArticle(
        @RequestParam title: String,
        @RequestParam childLink: String,
        @RequestParam parentLink: String
    ): String {
        val link = "$parentLink/$childLink"

        restClient.post()
            .uri("http://localhost:8081/article")
            .contentType(APPLICATION_JSON)
            .body(ArticleDto(title, link, parentLink))
            .retrieve()
            .toBodilessEntity()

        return "redirect:$link"
    }

    @GetMapping("/edit/{*link}")
    fun editArticle(@PathVariable link: String, model: Model): String {
        val pathSegments = link.split("/")
        val username = pathSegments[1]
        val startIndex = pathSegments.indexOfFirst { it.startsWith("home") }
        val sublist = pathSegments.subList(startIndex, pathSegments.size)
        val extractedPath = sublist.joinToString("/", prefix = "/")
        return "redirect:/$username/editor$extractedPath"
    }

    @GetMapping("/delete/{*link}")
    fun deleteArticle(@PathVariable link: String): String {
        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article$link")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        restClient.delete()
            .uri("http://localhost:8081/article/${currentArticle?.id}")
            .retrieve()
            .toBodilessEntity()

        return "redirect:${currentArticle?.parentLink}"
    }
}