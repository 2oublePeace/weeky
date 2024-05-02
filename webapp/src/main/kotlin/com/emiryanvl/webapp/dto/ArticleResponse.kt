package com.emiryanvl.webapp.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ArticleResponse (
    @JsonProperty("id")
    var id: Long = 0,
    @JsonProperty("title")
    var title: String = "123",
    @JsonProperty("link")
    var link: String = "123",
    @JsonProperty("parentLink")
    var parentLink: String = "123",
    @JsonProperty("content")
    var content: String = "123",
    @JsonProperty("childArticles")
    val childArticles: List<ArticleResponse> = emptyList()
)