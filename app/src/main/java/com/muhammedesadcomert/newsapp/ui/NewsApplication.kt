package com.muhammedesadcomert.newsapp.ui

import android.app.Application
import com.muhammedesadcomert.newsapp.data.local.ArticleDatabase
import com.muhammedesadcomert.newsapp.data.repository.ArticleRepository

class NewsApplication : Application() {
    val database: ArticleRepository by lazy { ArticleRepository(ArticleDatabase(this)) }
}