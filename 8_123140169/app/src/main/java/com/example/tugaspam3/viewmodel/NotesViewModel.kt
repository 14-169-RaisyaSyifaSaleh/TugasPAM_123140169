package com.example.tugaspam3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.model.NotesUiState
import com.example.tugaspam3.platform.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<NotesUiState> = _searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllNotes()
            } else {
                repository.searchNotes(query)
            }
        }
        .map { notes ->
            when {
                notes.isEmpty() -> NotesUiState.Empty
                else -> NotesUiState.Success(notes = notes, searchQuery = _searchQuery.value)
            }
        }
        .onStart { emit(NotesUiState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotesUiState.Loading
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(title, content)
        }
    }

    fun updateNote(id: Long, title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(title, content, id)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun toggleFavorite(id: Long, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !currentIsFavorite)
        }
    }

    fun getNoteById(id: Long): Flow<Note?> {
        return repository.getAllNotes().map { notes ->
            notes.find { it.id == id }
        }
    }
}
