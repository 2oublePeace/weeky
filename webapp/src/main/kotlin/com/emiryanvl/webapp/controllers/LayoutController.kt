package com.emiryanvl.webapp.controllers

import com.emiryanvl.webapp.dto.ArticleDto
import com.emiryanvl.webapp.dto.UserDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
class LayoutController(private val restClient: RestClient) {
    @ModelAttribute("servletPath")
    fun getRequestServletPath(request: HttpServletRequest): String {
        return request.servletPath
    }

    @ModelAttribute("menuArticles")
    fun getMenuArticles(request: HttpServletRequest): List<ArticleDto> {
        val user = getUserFromRequest(request)

        return user?.let {
            restClient.get()
                .uri("http://localhost:8081/article/menu/${it.username}")
                .accept(APPLICATION_JSON)
                .retrieve()
                .body<List<ArticleDto>>()
        } ?: emptyList()
    }

    @ModelAttribute("homeArticle")
    fun getHomeArticle(request: HttpServletRequest): ArticleDto? {
        val user = getUserFromRequest(request)
        return user?.let {
            restClient.get()
                .uri("http://localhost:8081/article/${it.username + HOME_LINK}")
                .accept(APPLICATION_JSON)
                .retrieve()
                .body<ArticleDto>()
        }
    }

    @ModelAttribute("username")
    fun getUsername(request: HttpServletRequest): String? {
        val user = getUserFromRequest(request)
        return user?.username
    }

    @ModelAttribute("breadcrumbs")
    fun getBreadcrumbs(request: HttpServletRequest): List<ArticleDto?> {
        val user = getUserFromRequest(request)
        var link = String()
        return user?.let {
            val homeSegmentIndex = request.servletPath.indexOf(HOME_LINK)
            request.servletPath
                .substring(homeSegmentIndex)
                .split(SPLITTER)
                .filter { it.isNotEmpty() }
                .map {
                    link += SPLITTER + it
                    restClient.get()
                        .uri("http://localhost:8081/article/${user.username}$link")
                        .accept(APPLICATION_JSON)
                        .retrieve()
                        .body<ArticleDto>()
                }
        } ?: emptyList()
    }

    private fun getUserFromRequest(request: HttpServletRequest): UserDto? {
        val pathSegments = request.servletPath.split(SPLITTER)
        val username = pathSegments[USERNAME_INDEX]
        return if(pathSegments.contains(HOME_SEGMENT)) {
            restClient.get()
                .uri("http://localhost:8081/user/$username")
                .accept(APPLICATION_JSON)
                .retrieve()
                .body<UserDto>()
        } else null
    }

    companion object {
        const val SPLITTER = "/"
        const val USERNAME_INDEX = 1
        const val HOME_LINK = "/home"
        const val HOME_SEGMENT = "home"
    }
}