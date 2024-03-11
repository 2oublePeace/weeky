package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
@RequestMapping("/")
class ArticleController(private val restClient: RestClient) {
    @GetMapping("/home/{*articleLink}")
    fun getArticle(@PathVariable articleLink: String, model: Model): String {
        val menuArticles = restClient.get()
            .uri("http://localhost:8081/article")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>()

        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article/home$articleLink")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        model.addAttribute("menuArticles", menuArticles)
        model.addAttribute("newArticle", ArticleDto())
        if (currentArticle != null) {
            model.addAttribute("currentLink", currentArticle.link)
            model.addAttribute("content", currentArticle.content)
        }

        return "article"
    }

    @GetMapping
    fun getRoot(): String = "redirect:/home"
}