package com.emiryanvl.persistence.unit

import com.emiryanvl.persistence.dto.requests.ArticleRequest
import com.emiryanvl.persistence.dto.responses.ArticleResponse
import com.emiryanvl.persistence.entities.ArticleEntity
import com.emiryanvl.persistence.mappers.ArticleMapper
import com.emiryanvl.persistence.repositories.ArticleRepository
import com.emiryanvl.persistence.services.impl.ArticleServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Date
import java.time.LocalDate
import java.util.Optional

@SpringBootTest
class ArticleServiceImplTest {
    private val articleRepository = mockk<ArticleRepository>()
    private val articleMapper = ArticleMapper()
    private val articleService = ArticleServiceImpl(articleRepository, articleMapper)

    @Test
    fun getArticleTest() {
        //Given
        val link = "/user/home/football"
        val expected = ArticleResponse(
            2L,
            "Футбол",
            "/user/home/football",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )
        val foundedArticle = ArticleEntity(
            "Футбол",
            "/user/home/football",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        ).also { it.id = 2L }

        //When
        every { articleRepository.findByLink(link) } returns Optional.ofNullable(foundedArticle)

        //Then
        val actual = articleService.getArticle(link)
        assertEquals(expected, actual)
    }

    @Test
    fun getMenuArticlesTest() {
        //Given
        val username = "user"
        val expected = mutableListOf(
            ArticleResponse(
                2L,
                "Футбол",
                "/user/home/football",
                "/user/home",
                "**Футбольный контент**",
                Date.valueOf(LocalDate.now())
            ),
            ArticleResponse(
                3L,
                "Хоккей",
                "/user/home/hockey",
                "/user/home",
                "**Хоккейный контент**",
                Date.valueOf(LocalDate.now())
            )
        )
        val foundedArticles = mutableListOf(
            ArticleEntity(
                "Футбол",
                "/user/home/football",
                "/user/home",
                "**Футбольный контент**",
                Date.valueOf(LocalDate.now())
            ).also { it.id = 2L },
            ArticleEntity(
                "Хоккей",
                "/user/home/hockey",
                "/user/home",
                "**Хоккейный контент**",
                Date.valueOf(LocalDate.now())
            ).also { it.id = 3L }
        )

        //When
        every { articleRepository.findMenuArticles(username) } returns foundedArticles

        //Then
        val actual = articleService.getMenuArticles(username)
        assertEquals(expected.size, actual.size)
        expected.forEach { assertEquals(it, actual[expected.indexOf(it)]) }
    }

    @Test
    fun createArticleTest() {
        //Given
        val request = ArticleRequest(
            "Футбол",
            "/user/home/football",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )
        val homeArticleEntity = ArticleEntity(
            "Главная страница",
            "/user/home",
            "/user",
            "**Главный контент**",
            Date.valueOf(LocalDate.now())
        ).also { it.id = 1 }
        val expected = ArticleResponse(
            2L,
            "Футбол",
            "/user/home/football",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )

        //When
        every { articleRepository.save(any()) } returns articleMapper.toArticleEntity(request).also {
            it.id = 2L
        }
        every { articleRepository.findByLink(request.parentLink) } returns Optional.ofNullable(homeArticleEntity)

        //Then
        val actual = articleService.createArticle(request)
        assertEquals(expected, actual)
    }

    @Test
    fun updateArticleTest() {
        //Given
        val id = 2L
        val request = ArticleRequest(
            "Футбол",
            "/user/home/soccer",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )
        val expected = ArticleResponse(
            2L,
            "Футбол",
            "/user/home/soccer",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        )
        val existArticle = ArticleEntity(
            "Футбол",
            "/user/home/soccer",
            "/user/home",
            "**Футбольный контент**",
            Date.valueOf(LocalDate.now())
        ).also { it.id = 2L }

        //When
        every { articleRepository.save(any()) } returns articleMapper.toArticleEntity(request).also {
            it.id = 2L
        }
        every { articleRepository.findById(id) } returns Optional.ofNullable(existArticle)

        //Then
        val actual = articleService.updateArticle(id, request)
        assertEquals(expected, actual)
    }

    @Test
    fun deleteArticleTest() {
        //Given
        val id = 1L

        //When
        every { articleRepository.deleteById(id) } returns Unit

        //Then
        articleService.deleteArticle(id)
        verify(exactly = 1) { articleService.deleteArticle(id) }
    }

    @Test
    fun searchArticleTest() {
        //Given
        val searchText = "Футбол"
        val username = "user"
        val foundedArticle = mutableListOf(
            ArticleEntity(
                "Футбол",
                "/user/home/football",
                "/user/home",
                "**Футбольный контент**",
                Date.valueOf(LocalDate.now())
            ).also { it.id = 2L }
        )
        val expected = mutableListOf(
            ArticleResponse(
                2L,
                "Футбол",
                "/user/home/football",
                "/user/home",
                "**Футбольный контент**",
                Date.valueOf(LocalDate.now())
            )
        )

        //When
        every { articleRepository.findArticlesBySearchText(searchText, username) } returns foundedArticle

        //Then
        val actual = articleService.searchArticles(searchText, username)
        assertEquals(expected.size, actual.size)
        expected.forEach { assertEquals(it, actual[expected.indexOf(it)]) }
    }
}