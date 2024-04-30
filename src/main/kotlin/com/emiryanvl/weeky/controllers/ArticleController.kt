package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
class ArticleController(private val restClient: RestClient) : LayoutController(restClient) {
    @Value("\${api.article}")
    private lateinit var articleApi: String

    @GetMapping("/{username}/home/{*link}")
    fun getArticle(@PathVariable username: String, @PathVariable link: String, model: Model): String {
        val article = restClient.get()
            .uri("$articleApi/$username/${HOME_SEGMENT + link}")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()

        model.addAttribute("currentArticle", article)

        return "article"
    }

    @PostMapping("/{username}/create")
    fun createArticle(
        @RequestParam title: String,
        @RequestParam childLink: String,
        @RequestParam parentLink: String,
        @PathVariable username: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): String {
        return if(userDetails.username.equals(username)) {
            val articleLink = "$parentLink/$childLink"
            val articleDto = ArticleDto(title, articleLink, parentLink)

            restClient.post()
                .uri(articleApi)
                .contentType(APPLICATION_JSON)
                .body(articleDto)
                .retrieve()
                .toBodilessEntity()

            "redirect:$articleLink"
        } else "error"
    }

    @GetMapping("/{username}/edit/{*link}")
    fun editArticle(
        @PathVariable username: String,
        @PathVariable link: String,
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model
    ): String {
        return if(userDetails.username.equals(username)) {
            val article = restClient.get()
                .uri("$articleApi/${username + link}")
                .accept(APPLICATION_JSON)
                .retrieve()
                .body<ArticleDto>()

            model.addAttribute("currentArticle", article)

            "editor"
        } else "error"
    }

    @GetMapping("/{username}/delete/{*link}")
    fun deleteArticle(
        @PathVariable link: String,
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable username: String,
    ): String {
        return if(userDetails.username.equals(username)) {
            val currentArticle = restClient.get()
                .uri(articleApi + link)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body<ArticleDto>()

            restClient.delete()
                .uri("$articleApi/${currentArticle?.id}")
                .retrieve()
                .toBodilessEntity()

            "redirect:${currentArticle?.parentLink}"
        } else "error"
    }

    @GetMapping("/{username}/search")
    fun searchArticle(@RequestParam searchText: String, model: Model, @PathVariable username: String): String {
        val searchArticles = restClient.get()
                .uri("http://localhost:8081/article/search?searchText=$searchText")
                .accept(APPLICATION_JSON)
                .retrieve()
                .body<List<ArticleDto>>() ?: emptyList()

        model.addAttribute("searchArticles", searchArticles)

        return "search"
    }

    companion object {
        const val HOME_SEGMENT = "home"
    }
}