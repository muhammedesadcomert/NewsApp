package com.muhammedesadcomert.newsapp.data.model

data class News(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int,
)
