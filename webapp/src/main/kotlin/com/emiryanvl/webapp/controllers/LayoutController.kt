package com.emiryanvl.webapp.controllers

import com.emiryanvl.webapp.dto.ArticleDto
import com.emiryanvl.webapp.dto.UserDto
import com.emiryanvl.webapp.services.ArticleService
import com.emiryanvl.webapp.services.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Controller
class LayoutController(
    private val articleService: ArticleService,
    private val userService: UserService
) {
    @Value("\${home-segment}")
    private lateinit var homeSegment: String

    @ModelAttribute("servletPath")
    fun getRequestServletPath(request: HttpServletRequest): String {
        return request.servletPath
    }

    @ModelAttribute("menuArticles")
    fun getMenuArticles(request: HttpServletRequest): List<ArticleDto> {
        val user = getUserFromRequest(request)

        return user?.let { articleService.getMenu(it.username) } ?: emptyList()
    }

    @ModelAttribute("homeArticle")
    fun getHomeArticle(request: HttpServletRequest): ArticleDto? {
        val user = getUserFromRequest(request)
        return user?.let {
            articleService.getArticle("${it.username}/$homeSegment")
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
            val homeSegmentIndex = request.servletPath.indexOf(SPLITTER + homeSegment)
            request.servletPath
                .substring(homeSegmentIndex)
                .split(SPLITTER)
                .filter { it.isNotEmpty() }
                .map {
                    link += SPLITTER + it
                    articleService.getArticle(user.username + link)
                }
        } ?: emptyList()
    }

    private fun getUserFromRequest(request: HttpServletRequest): UserDto? {
        val pathSegments = request.servletPath.split(SPLITTER)
        val username = pathSegments[USERNAME_INDEX]
        return if(pathSegments.contains(homeSegment)) {
            userService.getUser(username)
        } else null
    }

    companion object {
        const val SPLITTER = "/"
        const val USERNAME_INDEX = 1
    }
}