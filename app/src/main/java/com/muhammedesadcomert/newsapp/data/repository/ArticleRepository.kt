package com.muhammedesadcomert.newsapp.data.repository

import com.muhammedesadcomert.newsapp.data.local.ArticleDatabase
import com.muhammedesadcomert.newsapp.data.model.Article
import com.muhammedesadcomert.newsapp.data.remote.NewsAPI
import com.muhammedesadcomert.newsapp.data.util.Constants.Companion.API_KEY
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val database: ArticleDatabase,
    private val newsAPI: NewsAPI,
) {

    suspend fun getAllArticles(searchQuery: String, pageNumber: Int) =
        newsAPI.getNews(searchQuery, pageNumber, API_KEY)

    fun getFavoriteArticles() = database.articleDao().getArticles()

    suspend fun insert(article: Article) = database.articleDao().insert(article)

    suspend fun deleteArticle(article: Article) = database.articleDao().deleteArticle(article)
}