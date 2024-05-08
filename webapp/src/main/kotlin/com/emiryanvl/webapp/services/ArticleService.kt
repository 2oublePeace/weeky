package com.emiryanvl.webapp.services

import com.emiryanvl.webapp.dto.ArticleDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class ArticleService(private val restClient: RestClient) {
    @Value("\${api.article}")
    private lateinit var articleApi: String
    
    fun getArticle(link: String): ArticleDto? {
        return restClient.get()
            .uri("$articleApi/$link")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<ArticleDto>()
    }

    fun createArticle(article: ArticleDto) {
        restClient.post()
            .uri(articleApi)
            .contentType(APPLICATION_JSON)
            .body(article)
            .retrieve()
            .toBodilessEntity()
    }

    fun deleteArticle(id: Long) {
        restClient.delete()
            .uri("$articleApi/$id")
            .retrieve()
            .toBodilessEntity()
    }

    fun getMenu(username: String): List<ArticleDto> {
        return restClient.get()
            .uri("$articleApi/menu/$username")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body<List<ArticleDto>>() ?: emptyList()
    }

    companion object {
        const val HOME_SEGMENT = "home"
    }
}