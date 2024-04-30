package com.emiryanvl.persistence.mappers

import com.emiryanvl.persistence.dto.requests.ArticleRequest
import com.emiryanvl.persistence.dto.responses.ArticleResponse
import com.emiryanvl.persistence.entities.ArticleEntity
import org.springframework.stereotype.Component

@Component
class ArticleMapper {
    fun toArticleEntity(articleRequest: ArticleRequest): ArticleEntity {
        return ArticleEntity(
            articleRequest.title,
            articleRequest.link,
            articleRequest.parentLink,
            articleRequest.content,
            articleRequest.date
        )
    }

    fun toArticleResponse(articleEntity: ArticleEntity): ArticleResponse {
        if(articleEntity.childArticles.isNotEmpty()) {
            return ArticleResponse(
                articleEntity.id,
                articleEntity.title,
                articleEntity.link,
                articleEntity.parentLink,
                articleEntity.content,
                articleEntity.date,
                articleEntity.childArticles.map { toArticleResponse(it) }
            )
        }
        return ArticleResponse(
            articleEntity.id,
            articleEntity.title,
            articleEntity.link,
            articleEntity.parentLink,
            articleEntity.content,
            articleEntity.date
        )
    }
}