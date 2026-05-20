package com.example.tugaspam3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.data.SettingsManager
import com.example.tugaspam3.data.SortOrder
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.model.NotesUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing notes and their states.
 * Handles CRUD operations, search functionality, sort preferences, and API synchronization.
 */
class NotesViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    /**
     * Combined flow that reacts to both search query changes and sort order preferences.
     * Fulfills the "proper UI states" and "sort order" requirements.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<NotesUiState> = combine(
        _searchQuery,
        settingsManager.sortOrder
    ) { query, sortOrder ->
        Pair(query, sortOrder)
    }.flatMapLatest { (query, sortOrder) ->
        val baseFlow = if (query.isEmpty()) {
            repository.getAllNotes()
        } else {
            repository.searchNotes(query)
        }

        baseFlow.map { notes ->
            val sortedNotes = when (sortOrder) {
                SortOrder.BY_TITLE -> notes.sortedBy { it.title.lowercase() }
                SortOrder.BY_DATE -> notes.sortedByDescending { it.timestamp }
            }
            
            when {
                sortedNotes.isEmpty() -> NotesUiState.Empty
                else -> NotesUiState.Success(notes = sortedNotes, searchQuery = query)
            }
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

    fun syncNotes() {
        viewModelScope.launch {
            _isSyncing.value = true
            repository.syncWithRemote()
            _isSyncing.value = false
        }
    }

    fun getNoteById(id: Long): Flow<Note?> {
        return repository.getAllNotes().map { notes ->
            notes.find { it.id == id }
        }
    }
}
