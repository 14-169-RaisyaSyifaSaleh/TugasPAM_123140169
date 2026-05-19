package com.example.tugaspam3.model

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val timestamp: Long,
    val isFavorite: Boolean = false
)
