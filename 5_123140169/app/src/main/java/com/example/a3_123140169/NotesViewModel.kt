package com.example.a3_123140169

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    fun addNote(title: String, content: String) {
        _notes.update { currentNotes ->
            val newId = (currentNotes.maxOfOrNull { it.id } ?: 0) + 1
            currentNotes + Note(newId, title, content)
        }
    }

    fun updateNote(id: Int, title: String, content: String) {
        _notes.update { currentNotes ->
            currentNotes.map {
                if (it.id == id) it.copy(title = title, content = content) else it
            }
        }
    }

    fun toggleFavorite(id: Int) {
        _notes.update { currentNotes ->
            currentNotes.map {
                if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
            }
        }
    }

    fun deleteNote(id: Int) {
        _notes.update { it.filter { note -> note.id != id } }
    }
}
