package com.example.a6_123140169.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val results: List<Article>
)

@Serializable
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    val id: Int,
    val title: String,
    val summary: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("published_at")
    val publishedAt: String,
    val url: String
)
