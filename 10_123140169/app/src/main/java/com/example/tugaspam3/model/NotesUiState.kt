package com.example.tugaspam3.model

sealed interface NotesUiState {
    object Loading : NotesUiState
    data class Success(
        val notes: List<Note> = emptyList(),
        val searchQuery: String = ""
    ) : NotesUiState
    object Empty : NotesUiState
}
