package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
@RequestMapping("/{username}")
class PageController(private val restClient: RestClient) {
    @GetMapping("/home/{*link}")
    fun getArticle(@PathVariable username: String, @PathVariable link: String): String = "article"

    @GetMapping("/editor/{*link}")
    fun getEditor(@PathVariable username: String, @PathVariable link: String): String = "editor"

    @GetMapping("/search")
    fun searchArticle(@RequestParam searchText: String, model: Model): String {
        val searchArticles = restClient.get()
            .uri("http://localhost:8081/article/search?searchText=$searchText")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>() ?: emptyList()

        model.addAttribute("searchArticles", searchArticles)

        return "search"
    }

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
    fun getHomeArticle(@PathVariable username: String): ArticleDto {
        return restClient.get()
            .uri("http://localhost:8081/article/$username/home")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>() ?: ArticleDto(link = "/$username/home", title = "Главная страница")
    }

    @ModelAttribute("currentArticle")
    fun getCurrentArticle(@PathVariable username: String, request: HttpServletRequest): ArticleDto? {
        val link = getPathFragmentsFromHome(request.servletPath)
        return restClient.get()
            .uri("http://localhost:8081/article/$username$link")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()
    }

    @ModelAttribute("username")
    fun getUsername(@PathVariable username: String): String = username

    @ModelAttribute("breadcrumbs")
    fun getBreadcrumbs(@PathVariable username: String, request: HttpServletRequest): MutableList<ArticleDto>? {
        val articles = mutableListOf<ArticleDto>()
        var currentPath = ""
        val separator = "/"
        val pathFromHome = getPathFragmentsFromHome(request.servletPath)
        val pathFragments = pathFromHome?.apply { this.split(separator).filter { it.isNotEmpty() } }
        pathFragments?.let {
            for (fragment in pathFragments) {
                currentPath += separator + fragment
                val article = restClient.get()
                    .uri("http://localhost:8081/article/$username$currentPath")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body<ArticleDto>()
                article?.let { articles.add(it) }
            }
        }
        return articles
    }

    private fun getPathFragmentsFromHome(path: String): String? {
        val separator = "/"
        val homeFragmentName = "home"
        val pathSegments = path.split(separator)
        val startIndex = pathSegments.indexOfFirst { it.startsWith(homeFragmentName) }
        return if(startIndex > -1) {
            val sublist = pathSegments.subList(startIndex, pathSegments.size)
            sublist.joinToString(separator, prefix = separator)
        } else {
            return null
        }
    }
}