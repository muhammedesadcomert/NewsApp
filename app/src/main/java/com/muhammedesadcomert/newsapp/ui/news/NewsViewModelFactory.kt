package com.muhammedesadcomert.newsapp.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muhammedesadcomert.newsapp.data.repository.ArticleRepository

class NewsViewModelFactory(private val articleRepository: ArticleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(articleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}