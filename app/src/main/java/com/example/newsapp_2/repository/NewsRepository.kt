package com.example.newsapp_2.repository

import com.example.newsapp_2.API.RetrofitInstance
import com.example.newsapp_2.database.Database
import com.example.newsapp_2.models.Article

class NewsRepository(
    private val db: Database
) {

    suspend fun getBreakingNews(countryCode: String) = RetrofitInstance.api.getBreakingNews(countryCode)

    suspend fun searchNews(searchQuery: String) = RetrofitInstance.api.searchNews(searchQuery)

    suspend fun insertArticle(article: Article) = db.articleDao.insertArticle(article)

    suspend fun deleteArticle(article: Article) = db.articleDao.deleteArticle(article)

    fun getAllArticles() = db.articleDao.getAllArticles()
}