package com.muhammedesadcomert.appcentnewsapp.ui

import android.app.Application
import com.muhammedesadcomert.appcentnewsapp.data.local.ArticleDatabase
import com.muhammedesadcomert.appcentnewsapp.data.repository.ArticleRepository

class NewsApplication : Application() {
    val database: ArticleRepository by lazy { ArticleRepository(ArticleDatabase(this)) }
}