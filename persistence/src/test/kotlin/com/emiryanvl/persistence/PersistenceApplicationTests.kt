package com.emiryanvl.persistence

import com.emiryanvl.persistence.dto.requests.ArticleRequest
import com.emiryanvl.persistence.dto.requests.UserRequest
import com.emiryanvl.persistence.exceptions.NotFoundException
import com.emiryanvl.persistence.repositories.ArticleRepository
import com.emiryanvl.persistence.repositories.UserRepository
import com.emiryanvl.persistence.services.ArticleService
import com.emiryanvl.persistence.services.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Date
import java.time.LocalDate

@SpringBootTest
class PersistenceApplicationTests {

    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var articleService: ArticleService
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var articleRepository: ArticleRepository

    val userCreateRequest = UserRequest(
        "user",
        "password",
        "USER"
    )

    val articleCreateRequest = ArticleRequest(
        "Футбол",
        "/user/home/football",
        "/user/home",
        "**Футбольный контент**",
        Date.valueOf(LocalDate.now())
    )

    @Test
    fun createArticleTest() {
        userService.createUser(userCreateRequest)
        val createdArticle = articleService.createArticle(articleCreateRequest)
        assertNotNull(createdArticle.id)
        assertEquals(articleCreateRequest.title, createdArticle.title)
        assertEquals(articleCreateRequest.link, createdArticle.link)
        assertEquals(articleCreateRequest.parentLink, createdArticle.parentLink)
        assertEquals(articleCreateRequest.content, createdArticle.content)
        assertEquals(articleCreateRequest.date, createdArticle.date)

        cleanUp()
    }

    @Test
    fun getMenuArticlesTest() {
        userService.createUser(userCreateRequest)
        val createdArticle = articleService.createArticle(articleCreateRequest)
        val menuArticles = articleService.getMenuArticles(userCreateRequest.username)
        assertTrue(menuArticles.isNotEmpty())
        assertTrue(menuArticles.contains(createdArticle))

        cleanUp()
    }

    @Test
    fun updateArticleTest() {
        userService.createUser(userCreateRequest)
        val createdArticle = articleService.createArticle(articleCreateRequest)
        val articleUpdateRequest = ArticleRequest(
            "Хоккей",
            "/user/home/Хоккей",
            "/user/home",
            "**Хоккейный контент**",
            Date.valueOf(LocalDate.now())
        )
        val updatedArticle = createdArticle.id?.let { articleService.updateArticle(it, articleUpdateRequest) }
        assertNotNull(
            updatedArticle?.let {
                assertEquals(articleUpdateRequest.title, updatedArticle.title)
                assertEquals(articleUpdateRequest.link, updatedArticle.link)
                assertEquals(articleUpdateRequest.parentLink, updatedArticle.parentLink)
                assertEquals(articleUpdateRequest.content, updatedArticle.content)
                assertEquals(articleUpdateRequest.date, updatedArticle.date)
            }
        )

        cleanUp()
    }

    @Test
    fun deleteArticleTest() {
        userService.createUser(userCreateRequest)
        val articleDeleteRequest = articleService.createArticle(articleCreateRequest)
        articleRepository.deleteAll()
        assertThrows(NotFoundException::class.java) { articleService.getArticle(articleDeleteRequest.link) }

        cleanUp()
    }

    private fun cleanUp() {
        userRepository.deleteAll()
        articleRepository.deleteAll()
    }
}
