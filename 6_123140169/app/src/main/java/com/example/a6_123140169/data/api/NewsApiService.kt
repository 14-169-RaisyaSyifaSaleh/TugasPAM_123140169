package com.example.a6_123140169.data.api

import com.example.a6_123140169.data.model.NewsResponse
import io.ktor.client.call.*
import io.ktor.client.request.*

class NewsApiService {
    suspend fun getNews(): NewsResponse {
        return KtorClient.client.get("https://api.spaceflightnewsapi.net/v4/articles/").body()
    }
}
