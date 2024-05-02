package com.emiryanvl.persistence.dto.requests

import java.sql.Date

data class ArticleRequest (
    val title: String,
    val link: String,
    val parentLink: String,
    val content: String = "",
    val date: Date
)