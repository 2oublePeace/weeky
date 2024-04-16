package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
@RequestMapping("/{username}")
class PageController(private val restClient: RestClient) {
    @GetMapping("/home/{*link}")
    fun getArticle(@PathVariable username: String, @PathVariable link: String): String = "article"

    @GetMapping("/editor/{*link}")
    fun getEditor(@PathVariable username: String, @PathVariable link: String): String = "editor"

    @ModelAttribute("servletPath")
    fun getRequestServletPath(request: HttpServletRequest): String {
        return request.servletPath
    }

    @ModelAttribute("menuArticles")
    fun getMenuArticles(@PathVariable username: String, request: HttpServletRequest): List<ArticleDto> {
        return restClient.get()
            .uri("http://localhost:8081/article/menu/$username")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>() ?: emptyList()
    }

    @ModelAttribute("homeArticle")
    fun getHomeArticle(@PathVariable username: String, request: HttpServletRequest): ArticleDto {
        return restClient.get()
            .uri("http://localhost:8081/article/$username/home")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>() ?: ArticleDto(link = "/$username/home", title = "Главная страница")
    }

    @ModelAttribute("currentArticle")
    fun getCurrentArticle(@PathVariable username: String, request: HttpServletRequest): ArticleDto {
        val pathSegments = request.servletPath.split("/")
        val startIndex = pathSegments.indexOfFirst { it.startsWith("home") }
        val sublist = pathSegments.subList(startIndex, pathSegments.size)
        val link = sublist.joinToString("/", prefix = "/")

        return restClient.get()
            .uri("http://localhost:8081/article/$username$link")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()!!
    }

    @ModelAttribute("username")
    fun getUsername(@PathVariable username: String): String = username
}