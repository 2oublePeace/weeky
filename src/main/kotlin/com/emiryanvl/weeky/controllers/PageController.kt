package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
class PageController(private val restClient: RestClient) {
    @GetMapping("/{username}/home/{*link}")
    fun getArticle(@PathVariable username: String, @PathVariable link: String): String = "article"

    @GetMapping("/editor/{*link}")
    fun getEditor(@PathVariable link: String): String = "editor"

    @ModelAttribute("servletPath")
    fun getRequestServletPath(request: HttpServletRequest): String {
        return request.servletPath
    }

    @ModelAttribute("menuArticles")
    fun getMenuArticles(request: HttpServletRequest): List<ArticleDto> {
        val pathSegments = request.servletPath.split("/")
        val startIndex = pathSegments.indexOfFirst { it.startsWith("home") }
        val sublist = pathSegments.subList(startIndex - 1, pathSegments.size)
        val link = sublist.joinToString("/", prefix = "/")
        val username = link.split("/")[1]

        return restClient.get()
            .uri("http://localhost:8081/article/menu/$username")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>() ?: emptyList()
    }

    @ModelAttribute("homeArticle")
    fun getHomeArticle(request: HttpServletRequest): ArticleDto {
        val pathSegments = request.servletPath.split("/")
        val startIndex = pathSegments.indexOfFirst { it.startsWith("home") }
        val sublist = pathSegments.subList(startIndex - 1, pathSegments.size)
        val link = sublist.joinToString("/", prefix = "/")
        val username = link.split("/")[1]

        return restClient.get()
            .uri("http://localhost:8081/article/$username/home")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>() ?: ArticleDto(link = "/$username/home", title = "Главная страница")
    }

    @ModelAttribute("currentArticle")
    fun getCurrentArticle(request: HttpServletRequest): ArticleDto {
        val pathSegments = request.servletPath.split("/")
        val startIndex = pathSegments.indexOfFirst { it.startsWith("home") }
        val sublist = pathSegments.subList(startIndex - 1, pathSegments.size)
        val link = sublist.joinToString("/", prefix = "/")

        return restClient.get()
            .uri("http://localhost:8081/article/$link")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()!!
    }
}