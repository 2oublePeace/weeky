package com.emiryanvl.persistence.repositories

import com.emiryanvl.persistence.entities.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ArticleRepository : JpaRepository<ArticleEntity, Long> {
    @Query(
        "SELECT a FROM ArticleEntity a WHERE " +
                "(LOWER(a.content) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchText, '%'))) AND " +
                "a.link LIKE CONCAT('/' , :username, '%')"
    )
    fun findArticlesBySearchText(searchText: String, username: String): List<ArticleEntity>
    fun findByLink(link: String): Optional<ArticleEntity>
    @Query("select a from ArticleEntity a where a.parentLink = CONCAT('/' , :username, '/home')")
    fun findMenuArticles(username: String): List<ArticleEntity>
}