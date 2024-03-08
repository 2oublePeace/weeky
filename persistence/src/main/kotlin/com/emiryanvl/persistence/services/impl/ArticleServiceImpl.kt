package com.emiryanvl.persistence.services.impl

import com.emiryanvl.persistence.dto.requests.ArticleRequest
import com.emiryanvl.persistence.dto.responses.ArticleResponse
import com.emiryanvl.persistence.entities.ArticleEntity
import com.emiryanvl.persistence.exceptions.NotFoundException.Companion.notFoundException
import com.emiryanvl.persistence.mappers.ArticleMapper
import com.emiryanvl.persistence.repositories.ArticleRepository
import com.emiryanvl.persistence.services.ArticleService
import org.springframework.stereotype.Service

@Service
class ArticleServiceImpl(
        private val articleRepository: ArticleRepository,
        private val articleMapper: ArticleMapper
    ) : ArticleService {
    override fun getArticle(link: String): ArticleResponse {
        val temp = articleRepository.findByLink(link).orElseThrow(
        notFoundException("Статья не найдена")
        )
        return articleMapper.toArticleResponse(
            temp
        )
    }

    override fun getArticles(): List<ArticleResponse> {
        val a = articleRepository.findRootArticles().map { articleMapper.toArticleResponse(it) }
        return a
    }

    override fun createArticle(articleRequest: ArticleRequest): ArticleResponse {
        return articleMapper.toArticleResponse(
            articleRepository.save(
                complementArticle(
                    articleMapper.toArticleEntity(articleRequest),
                    articleRequest
                )
            )
        )
    }

    override fun updateArticle(id: Long, articleRequest: ArticleRequest): ArticleResponse {
        return articleMapper.toArticleResponse(
            articleRepository.save(
                editArticle(
                    articleRepository.findById(id).orElseThrow(notFoundException("Пользователь не найден")),
                    articleRequest
                )
            )
        )
    }

    override fun deleteArticle(id: Long) {
        articleRepository.deleteById(id)
    }

    private fun complementArticle(articleEntity: ArticleEntity, articleRequest: ArticleRequest): ArticleEntity {
        return articleEntity.apply {
            this.parentArticle = articleRepository.findByLink(articleRequest.parentLink).orElseThrow(
                notFoundException("Статья не найдена")
            )
        }
    }

    private fun editArticle(articleEntity: ArticleEntity, articleRequest: ArticleRequest): ArticleEntity {
        return articleEntity.apply {
            articleEntity.title = articleRequest.title
            articleEntity.content = articleRequest.content
            articleEntity.link = articleRequest.link
            articleEntity.parentLink = articleRequest.parentLink
        }
    }
}