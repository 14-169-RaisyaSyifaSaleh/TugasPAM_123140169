package com.example.a6_123140169.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a6_123140169.data.model.Article
import com.example.a6_123140169.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            repository.getNews()
                .catch { e ->
                    _uiState.value = NewsUiState.Error(e.message ?: "Unknown error")
                }
                .collect { result ->
                    result.onSuccess { articles ->
                        _uiState.value = NewsUiState.Success(articles.shuffled())
                    }.onFailure { exception ->
                        _uiState.value = NewsUiState.Error(exception.message ?: "Failed to load news")
                    }
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val result = repository.refreshNews()
            
            result.onSuccess {
                // Jika sukses, data di DB sudah terupdate, Flow dari repository.getNews() 
                // di fetchNews() akan otomatis terupdate jika Anda menggunakan Room Flow.
                // Jika tidak, kita acak data yang ada sekarang.
                val currentState = _uiState.value
                if (currentState is NewsUiState.Success) {
                    _uiState.value = NewsUiState.Success(currentState.articles.shuffled())
                }
            }.onFailure { exception ->
                // ERROR STATE: Jika refresh gagal, tampilkan UI Error
                _uiState.value = NewsUiState.Error("Refresh failed: ${exception.message ?: "No internet connection"}")
            }
            
            _isRefreshing.value = false
        }
    }

    class Factory(private val repository: NewsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
