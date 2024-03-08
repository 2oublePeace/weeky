package com.emiryanvl.persistence.entities

import jakarta.persistence.*

@Entity
@Table(name = "_article")
class ArticleEntity (
    var title: String,
    var link: String,
    @Column(name = "parent_link")
    var parentLink: String,
    var content: String,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "parentArticle")
    var childArticles: List<ArticleEntity> = emptyList(),
    @ManyToOne
    @JoinColumn(name="parent_article_id")
    var parentArticle: ArticleEntity? = null
) : BaseEntity<Long>()