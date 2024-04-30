package com.emiryanvl.persistence.dto.responses

import com.fasterxml.jackson.annotation.JsonIgnore
import java.sql.Date

data class ArticleResponse(
    val id: Long? = null,
    val title: String,
    val link: String,
    val parentLink: String,
    val content: String = "",
    val date: Date,
    val childArticles: List<ArticleResponse> = emptyList()
)