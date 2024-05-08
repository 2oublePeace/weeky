package com.emiryanvl.webapp.controllers

import com.emiryanvl.webapp.dto.ArticleDto
import com.emiryanvl.webapp.services.ArticleService
import com.emiryanvl.webapp.services.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class ArticleController(
    private val articleService: ArticleService,
    private val userService: UserService
) : LayoutController(articleService, userService) {
    @Value("\${home-segment}")
    private lateinit var homeSegment: String

    @GetMapping("/{username}/home/{*link}")
    fun getArticle(@PathVariable username: String, @PathVariable link: String, model: Model): String {
        val article = articleService.getArticle("$username/${homeSegment + link}")
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
            articleService.createArticle(articleDto)
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
        return if (userDetails.username.equals(username)) {
            val article = articleService.getArticle("$username/$link")
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
            val currentArticle = articleService.getArticle("$username/$link")
            currentArticle?.id?.let { articleService.deleteArticle(it) }
            "redirect:${currentArticle?.parentLink}"
        } else "error"
    }
}