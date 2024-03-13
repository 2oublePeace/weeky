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
    @GetMapping("/home/{*articleLink}")
    fun getArticle(@PathVariable articleLink: String, model: Model): String {
        val menuArticles = restClient.get()
            .uri("http://localhost:8081/article")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>()

        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article/article/home$articleLink")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        model.addAttribute("menuArticles", menuArticles)
        currentArticle?.let {
            model.addAttribute("currentLink", currentArticle.link)
            model.addAttribute("content", currentArticle.content)
        }

        return "article"
    }

    @PostMapping("/create")
    fun createArticle(@RequestParam childLink: String,
                      @RequestParam title: String,
                      @RequestParam parentLink: String): String {
        val link = "$parentLink/$childLink"
        restClient.post()
            .uri("http://localhost:8081/article")
            .contentType(APPLICATION_JSON)
            .body(ArticleDto(title, link, parentLink))
            .retrieve()
            .toBodilessEntity()
        return "redirect:$link"
    }

    @DeleteMapping("/delete")
    fun deleteArticle(@RequestParam parentLink: String, articleDto: ArticleDto): String {
        articleDto.link = parentLink + articleDto.link
        restClient.delete()
            .uri("http://localhost:8081/article")
            .retrieve()
            .toBodilessEntity()
        return "redirect:$parentLink"
    }
}