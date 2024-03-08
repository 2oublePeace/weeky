package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
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
    fun getHomeArticle(model: Model): String {
        model.addAttribute("menuArticles", mutableListOf(
                ArticleDto(
                    childArticles = mutableListOf(
                        ArticleDto(
                            childArticles = mutableListOf(
                                ArticleDto()
                            )
                        )
                    )
                )
            )
        )
        return "article"
    }

    @GetMapping
    fun getRoot(): String {
        return "redirect:/home"
    }
}