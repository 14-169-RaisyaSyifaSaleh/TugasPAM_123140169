package com.example.tugaspam3.data

import retrofit2.http.GET

data class RemoteNote(
    val id: Long,
    val title: String,
    val body: String,
    val userId: Int
)

interface ApiService {
    @GET("posts?_limit=5")
    suspend fun getRemoteNotes(): List<RemoteNote>
}
