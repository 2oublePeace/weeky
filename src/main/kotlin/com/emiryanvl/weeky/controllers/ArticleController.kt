package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
@RequestMapping("{username}")
class ArticleController(private val restClient: RestClient) {
    @GetMapping("/home/{*articleLink}")
    fun getArticle(@PathVariable username: String, @PathVariable articleLink: String, model: Model): String {
        val menuArticles = restClient.get()
            .uri("http://localhost:8081/article/menu/$username")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>()

        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article/$username/home$articleLink")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        val homeArticle = restClient.get()
            .uri("http://localhost:8081/article/$username/home")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        model.addAttribute("menuArticles", menuArticles)
        model.addAttribute("username", username)
        homeArticle?.let {
            model.addAttribute("homeArticleTitle", it.title)
            model.addAttribute("homeArticleLink", it.link)
        }
        currentArticle?.let {
            model.addAttribute("currentLink", it.link)
            model.addAttribute("content", it.content)
        }

        return "article"
    }

    @GetMapping("/edit")
    fun editArticle(@PathVariable username: String, @RequestParam currentLink: String, model: Model): String {
        val menuArticles = restClient.get()
            .uri("http://localhost:8081/article/menu/$username")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>()

        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article$currentLink")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        val homeArticle = restClient.get()
            .uri("http://localhost:8081/article/$username/home")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        model.addAttribute("menuArticles", menuArticles)
        model.addAttribute("username", username)
        homeArticle?.let {
            model.addAttribute("homeArticleTitle", it.title)
            model.addAttribute("homeArticleLink", it.link)
        }
        currentArticle?.let {
            model.addAttribute("title", it.title)
            model.addAttribute("currentLink", it.link)
            model.addAttribute("content", it.content)
        }

        return "editor"
    }

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

    @GetMapping("/delete")
    fun deleteArticle(@RequestParam currentLink: String): String {
        val currentArticle = restClient.get()
            .uri("http://localhost:8081/article$currentLink")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        currentArticle?.let {
            restClient.delete()
                .uri("http://localhost:8081/article/${it.id}")
                .retrieve()
                .toBodilessEntity()
        }

        return "redirect:${currentArticle?.parentLink}"
    }
}