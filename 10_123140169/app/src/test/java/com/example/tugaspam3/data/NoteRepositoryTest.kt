package com.example.tugaspam3.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.tugaspam3.db.NoteDatabase
import com.example.tugaspam3.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NoteRepositoryTest {
    private lateinit var repository: NoteRepository
    private lateinit var database: NoteDatabase

    @Before
    fun setup() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        NoteDatabase.Schema.create(driver)
        database = NoteDatabase(driver)
        repository = NoteRepository(database)
    }

    @Test
    fun `insertNote should add note to database`() = runBlocking {
        repository.insertNote("Test Title", "Test Content")
        val notes = repository.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("Test Title", notes[0].title)
    }

    @Test
    fun `deleteNote should remove note from database`() = runBlocking {
        repository.insertNote("To Delete", "Content")
        val initialNotes = repository.getAllNotes().first()
        val id = initialNotes[0].id

        repository.deleteNote(id)
        val finalNotes = repository.getAllNotes().first()
        assertTrue(finalNotes.isEmpty())
    }

    @Test
    fun `toggleFavorite should update isFavorite status`() = runBlocking {
        repository.insertNote("Fav Note", "Content")
        val id = repository.getAllNotes().first()[0].id

        repository.toggleFavorite(id, true)
        val note = repository.getAllNotes().first()[0]
        assertTrue(note.isFavorite)
    }

    @Test
    fun `searchNotes should return matching results`() = runBlocking {
        repository.insertNote("Apple", "Fruit")
        repository.insertNote("Banana", "Fruit")
        
        val results = repository.searchNotes("Apple").first()
        assertEquals(1, results.size)
        assertEquals("Apple", results[0].title)
    }

    @Test
    fun `getAllNotes should return all notes in database`() = runBlocking {
        repository.insertNote("Note 1", "Content 1")
        repository.insertNote("Note 2", "Content 2")
        
        val notes = repository.getAllNotes().first()
        assertEquals(2, notes.size)
    }
}
