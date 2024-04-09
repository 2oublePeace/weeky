package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
@RequestMapping("/editor")
class EditorController(private val restClient: RestClient) {
    @GetMapping
    fun editArticle(@RequestParam currentLink: String, model: Model): String {
        val menuArticles = restClient.get()
            .uri("http://localhost:8081/article")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>()

        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article$currentLink")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        model.addAttribute("menuArticles", menuArticles)
        currentArticle?.let {
            model.addAttribute("currentLink", currentArticle.link)
            model.addAttribute("content", currentArticle.content)
        }

        return "editor"
    }

    @PostMapping("/save")
    fun saveArticle(@RequestParam link: String, @RequestParam content: String,  model: Model): String {
        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article$link")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        currentArticle?.let {
            currentArticle.content = content
            restClient.put()
                .uri("http://localhost:8081/article$link")
                .contentType(MediaType.APPLICATION_JSON)
                .body(it)
                .retrieve()
                .toBodilessEntity()
        }

        return "redirect:$link"
    }
}