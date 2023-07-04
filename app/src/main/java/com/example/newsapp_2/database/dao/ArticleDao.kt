package com.example.newsapp_2.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp_2.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM article")
    fun getAllArticles(): LiveData<List<Article>>

}