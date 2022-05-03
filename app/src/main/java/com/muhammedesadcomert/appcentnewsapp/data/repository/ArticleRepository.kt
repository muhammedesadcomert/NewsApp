package com.muhammedesadcomert.appcentnewsapp.data.repository

import com.muhammedesadcomert.appcentnewsapp.data.local.ArticleDatabase
import com.muhammedesadcomert.appcentnewsapp.data.model.Article
import com.muhammedesadcomert.appcentnewsapp.data.remote.NewsClient
import com.muhammedesadcomert.appcentnewsapp.data.util.Constants.Companion.API_KEY

class ArticleRepository(private val database: ArticleDatabase) {

    suspend fun getAllArticles(searchQuery: String, pageNumber: Int) =
        NewsClient.API.getNews(searchQuery, pageNumber, API_KEY)

    fun getFavoriteArticles() = database.articleDao().getArticles()

    suspend fun insert(article: Article) = database.articleDao().insert(article)

    suspend fun deleteArticle(article: Article) = database.articleDao().deleteArticle(article)
}