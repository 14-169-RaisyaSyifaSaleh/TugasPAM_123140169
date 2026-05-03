package com.example.a6_123140169.data.repository

import com.example.a6_123140169.data.api.NewsApiService
import com.example.a6_123140169.data.local.NewsDao
import com.example.a6_123140169.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) {
    /**
     * Gets news from local database and refreshes from API.
     * This implements the "Offline caching with local storage" bonus.
     */
    fun getNews(): Flow<Result<List<Article>>> = flow {
        // First, emit what we have in the database
        // We use map to wrap the list in a Result
        val localData = newsDao.getAllArticles().map { Result.success(it) }
        
        try {
            // Fetch from network
            val response = apiService.getNews()
            // Update local database
            newsDao.deleteAllArticles()
            newsDao.insertArticles(response.results)
            // The localData flow will automatically emit the new data
            emitAll(localData)
        } catch (e: Exception) {
            // If network fails, still emit local data but maybe with an error or just the local data
            // For simplicity, if there's local data, we show it, otherwise show error
            emitAll(localData)
            // If the database is empty and network failed, we might want to emit a failure
            // but for now, we rely on the UI state to handle empty list or previous state
        }
    }
    
    suspend fun refreshNews(): Result<Unit> {
        return try {
            val response = apiService.getNews()
            newsDao.deleteAllArticles()
            newsDao.insertArticles(response.results)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
