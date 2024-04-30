package com.emiryanvl.persistence.services

import com.emiryanvl.persistence.dto.requests.ArticleRequest
import com.emiryanvl.persistence.dto.responses.ArticleResponse

interface ArticleService {
    fun getArticle(link: String): ArticleResponse
    fun getMenuArticles(username: String): List<ArticleResponse>
    fun createArticle(articleRequest: ArticleRequest): ArticleResponse
    fun updateArticle(id: Long, articleRequest: ArticleRequest) : ArticleResponse
    fun deleteArticle(id: Long)
    fun searchArticles(searchText: String): List<ArticleResponse>
}