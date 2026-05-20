package com.example.tugaspam3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugaspam3.data.GeminiService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface AiUiState {
    object Idle : AiUiState
    object Loading : AiUiState
    data class Success(val output: String) : AiUiState
    data class Error(val message: String) : AiUiState
}

class AiViewModel(private val geminiService: GeminiService) : ViewModel() {

    private val _uiState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val chatHistory: StateFlow<List<Pair<String, String>>> = _chatHistory.asStateFlow()

    fun summarizeNote(content: String) {
        viewModelScope.launch {
            _uiState.value = AiUiState.Loading
            try {
                val result = geminiService.summarizeNote(content)
                if (result != null) {
                    _uiState.value = AiUiState.Success(result)
                } else {
                    _uiState.value = AiUiState.Error("Failed to get summary. Please try again.")
                }
            } catch (e: Exception) {
                _uiState.value = AiUiState.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    fun sendMessage(noteContent: String, userMessage: String) {
        viewModelScope.launch {
            val currentHistory = _chatHistory.value.toMutableList()
            if (currentHistory.isEmpty()) {
                // Initialize context with the note content
                currentHistory.add("user" to "Here is the content of the note I want to discuss: $noteContent")
                currentHistory.add("model" to "I've read the note. How can I help you with it?")
            }
            
            currentHistory.add("user" to userMessage)
            _chatHistory.value = currentHistory
            
            _uiState.value = AiUiState.Loading
            
            try {
                var fullResponse = ""
                geminiService.chatWithNote(currentHistory.dropLast(1), userMessage)
                    .onEach { partialResponse ->
                        fullResponse += partialResponse
                        _uiState.value = AiUiState.Success(fullResponse)
                    }
                    .onCompletion {
                        if (fullResponse.isNotEmpty()) {
                            _chatHistory.value = _chatHistory.value + ("model" to fullResponse)
                            _uiState.value = AiUiState.Idle
                        }
                    }
                    .catch { e ->
                        _uiState.value = AiUiState.Error(e.localizedMessage ?: "Chat error")
                    }
                    .collect()
            } catch (e: Exception) {
                _uiState.value = AiUiState.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    fun clearState() {
        _uiState.value = AiUiState.Idle
        _chatHistory.value = emptyList()
    }
}
