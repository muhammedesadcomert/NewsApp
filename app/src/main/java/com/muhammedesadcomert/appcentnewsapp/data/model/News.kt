package com.muhammedesadcomert.appcentnewsapp.data.model

data class News(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int,
)
