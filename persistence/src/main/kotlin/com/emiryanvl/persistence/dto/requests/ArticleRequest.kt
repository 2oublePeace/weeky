package com.emiryanvl.persistence.dto.requests

data class ArticleRequest (
    val title: String,
    val link: String,
    val parentLink: String,
    val content: String = ""
)