package com.example.tugaspam3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugaspam3.data.GeminiService
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.model.NotesUiState
import com.example.tugaspam3.platform.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository,
    private val networkMonitor: NetworkMonitor,
    private val geminiService: GeminiService
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _aiState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val aiState: StateFlow<AiUiState> = _aiState.asStateFlow()

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

    // --- AI SECTION WITH RETRY LOGIC & STREAMING ---

    fun summarizeNote(content: String, retryCount: Int = 3) {
        viewModelScope.launch {
            _aiState.value = AiUiState.Loading
            var currentAttempt = 0
            var success = false
            
            while (currentAttempt < retryCount && !success) {
                try {
                    var fullText = ""
                    geminiService.summarizeNoteStream(content)
                        .onStart { _aiState.value = AiUiState.Streaming("") }
                        .collect { chunk ->
                            fullText += chunk
                            _aiState.value = AiUiState.Streaming(fullText)
                        }
                    _aiState.value = AiUiState.Success(fullText)
                    success = true
                } catch (e: Exception) {
                    currentAttempt++
                    if (currentAttempt >= retryCount) {
                        _aiState.value = AiUiState.Error("Gagal setelah $retryCount percobaan: ${e.message}")
                    } else {
                        delay(1000) // Wait before retry
                    }
                }
            }
        }
    }

    // Multi-turn conversation setup
    private var isChatInitialized = false
    
    fun askAi(noteContent: String, question: String, retryCount: Int = 3) {
        viewModelScope.launch {
            if (!isChatInitialized) {
                geminiService.startNewSession(noteContent)
                isChatInitialized = true
            }

            _aiState.value = AiUiState.Loading
            var currentAttempt = 0
            var success = false

            while (currentAttempt < retryCount && !success) {
                try {
                    var fullText = ""
                    geminiService.sendMessageStream(question)
                        .onStart { _aiState.value = AiUiState.Streaming("") }
                        .collect { chunk ->
                            fullText += chunk
                            _aiState.value = AiUiState.Streaming(fullText)
                        }
                    _aiState.value = AiUiState.Success(fullText)
                    success = true
                } catch (e: Exception) {
                    currentAttempt++
                    if (currentAttempt >= retryCount) {
                        _aiState.value = AiUiState.Error("Gagal menjawab: ${e.message}")
                    } else {
                        delay(1000)
                    }
                }
            }
        }
    }

    fun resetAiState() {
        _aiState.value = AiUiState.Idle
        isChatInitialized = false
    }
}

sealed class AiUiState {
    object Idle : AiUiState()
    object Loading : AiUiState()
    data class Streaming(val partialResponse: String) : AiUiState()
    data class Success(val response: String) : AiUiState()
    data class Error(val message: String) : AiUiState()
}
