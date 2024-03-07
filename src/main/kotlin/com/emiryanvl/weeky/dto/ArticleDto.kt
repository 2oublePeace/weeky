package com.emiryanvl.weeky.dto

data class ArticleDto (
        val title: String = "123",
        val link: String = "123",
        val parentLink: String = "123",
        val content: String = "123",
        val childArticles: List<ArticleDto> = emptyList()
)