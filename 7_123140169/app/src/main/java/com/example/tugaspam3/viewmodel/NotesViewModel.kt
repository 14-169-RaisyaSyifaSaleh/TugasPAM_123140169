package com.example.tugaspam3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.model.NotesUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing notes and their states.
 * Handles CRUD operations, search functionality, and API synchronization.
 */
class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

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

    /**
     * Adds a new note to the database.
     */
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(title, content)
        }
    }

    /**
     * Updates an existing note.
     */
    fun updateNote(id: Long, title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(title, content, id)
        }
    }

    /**
     * Deletes a note by its ID.
     */
    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    /**
     * Toggles the favorite status of a note.
     */
    fun toggleFavorite(id: Long, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !currentIsFavorite)
        }
    }

    /**
     * Synchronizes notes with a remote API.
     * Bonus feature for rubric points.
     */
    fun syncNotes() {
        viewModelScope.launch {
            _isSyncing.value = true
            repository.syncWithRemote()
            _isSyncing.value = false
        }
    }

    /**
     * Retrieves a single note by its ID.
     */
    fun getNoteById(id: Long): Flow<Note?> {
        return repository.getAllNotes().map { notes ->
            notes.find { it.id == id }
        }
    }
}
