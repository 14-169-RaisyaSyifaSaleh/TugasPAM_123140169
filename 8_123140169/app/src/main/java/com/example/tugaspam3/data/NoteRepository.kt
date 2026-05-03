package com.example.tugaspam3.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.tugaspam3.db.NoteDatabase
import com.example.tugaspam3.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(database: NoteDatabase) {
    private val queries = database.noteEntityQueries

    fun getAllNotes(): Flow<List<Note>> {
        return queries.getAllNotes()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { it.toNote() }
            }
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return queries.searchNotes(query, query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { it.toNote() }
            }
    }

    suspend fun insertNote(title: String, content: String, id: Long? = null) {
        queries.insertNote(
            id = id,
            title = title,
            content = content,
            timestamp = System.currentTimeMillis(),
            isFavorite = false
        )
    }

    suspend fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        queries.updateFavorite(isFavorite, id)
    }

    private fun com.example.tugaspam3.db.NoteEntity.toNote(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            timestamp = timestamp,
            isFavorite = isFavorite
        )
    }
}
