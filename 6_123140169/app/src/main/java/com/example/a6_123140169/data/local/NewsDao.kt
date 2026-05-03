package com.example.a6_123140169.data.local

import androidx.room.*
import com.example.a6_123140169.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()
}
