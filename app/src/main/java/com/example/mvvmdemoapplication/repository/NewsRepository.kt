package com.example.mvvmdemoapplication.repository

import com.example.mvvmdemoapplication.data.db.ArticleDatabase
import com.example.mvvmdemoapplication.data.retrofit.RetrofitInstance
import com.example.mvvmdemoapplication.model.Article

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.newsAPI.getHeadlines(countryCode, pageNumber)

    suspend fun searchForNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.newsAPI.searchForNews(searchQuery, pageNumber)

    suspend fun insertArticle(article: Article) = db.getArticleDao().insertArticle(article)

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun getSavedArticles() = db.getArticleDao().getSavedArticles()
}