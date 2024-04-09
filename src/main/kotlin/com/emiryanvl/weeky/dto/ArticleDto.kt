package com.emiryanvl.weeky.dto

data class ArticleDto (
    var title: String = "123",
    var link: String = "123",
    var parentLink: String = "123",
    var content: String = "123",
    val childArticles: List<ArticleDto> = emptyList(),
    var id: Long? = null
)