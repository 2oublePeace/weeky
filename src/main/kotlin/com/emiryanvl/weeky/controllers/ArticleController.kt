package com.emiryanvl.weeky.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class ArticleController {
    @GetMapping("/home/{*articleLink}")
    fun getArticle(@PathVariable articleLink: String): String {
        return "article"
    }

    @GetMapping("/home")
    fun getHomeArticle(): String {
        return "article"
    }

    @GetMapping
    fun getRoot(): String {
        return "redirect:/home"
    }
}