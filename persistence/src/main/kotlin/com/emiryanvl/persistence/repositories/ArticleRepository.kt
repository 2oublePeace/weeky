package com.emiryanvl.persistence.repositories

import com.emiryanvl.persistence.entities.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ArticleRepository : JpaRepository<ArticleEntity, Long> {
    @Query("select a from ArticleEntity a where a.link = :link")
    fun findByLink(link: String): Optional<ArticleEntity>
    @Query("select a from ArticleEntity a where a.parentLink = '/home'")
    fun findMenuArticles(): List<ArticleEntity>
}