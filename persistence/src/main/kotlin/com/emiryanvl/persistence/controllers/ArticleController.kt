package com.emiryanvl.persistence.controllers

import com.emiryanvl.persistence.dto.requests.ArticleRequest
import com.emiryanvl.persistence.dto.responses.ArticleResponse
import com.emiryanvl.persistence.services.ArticleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/article")
class ArticleController(private val articleService: ArticleService) {
    @GetMapping("/{*link}")
    fun getArticle(@PathVariable link: String) : ArticleResponse {
        return articleService.getArticle(link)
    }

    @GetMapping
    fun getMenuArticles() : ResponseEntity<List<ArticleResponse>> {
        return ResponseEntity<List<ArticleResponse>>(articleService.getMenuArticles(), HttpStatus.OK)
    }

    @PostMapping
    fun createArticle(@RequestBody articleRequest: ArticleRequest) : ResponseEntity<ArticleResponse> {
        return ResponseEntity<ArticleResponse>(articleService.createArticle(articleRequest), HttpStatus.CREATED)
    }

    @PutMapping("/{*link}")
    fun updateArticle(@PathVariable link: String, @RequestBody articleRequest: ArticleRequest): ResponseEntity<ArticleResponse> {
        return ResponseEntity<ArticleResponse>(articleService.updateArticle(link, articleRequest), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteArticle(@PathVariable id: Long) : ResponseEntity<Unit> {
        return ResponseEntity<Unit>(articleService.deleteArticle(id), HttpStatus.NO_CONTENT)
    }
}