package com.example.tugaspam3.viewmodel

import app.cash.turbine.test
import com.example.tugaspam3.data.GeminiService
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.model.NotesUiState
import com.example.tugaspam3.platform.NetworkMonitor
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val repository = mockk<NoteRepository>(relaxed = true)
    private val networkMonitor = mockk<NetworkMonitor>(relaxed = true)
    private val geminiService = mockk<GeminiService>(relaxed = true)
    
    private lateinit var viewModel: NotesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { networkMonitor.isConnected } returns flowOf(true)
        viewModel = NotesViewModel(repository, networkMonitor, geminiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should emit Success when repository has notes`() = runTest {
        // Arrange
        val notes = listOf(Note(1, "Title", "Content", 0L, false))
        every { repository.getAllNotes() } returns flowOf(notes)

        // Act & Assert (Flow Test with Turbine)
        viewModel.uiState.test {
            assertEquals(NotesUiState.Loading, awaitItem()) // Initial state
            val result = awaitItem()
            assertTrue(result is NotesUiState.Success)
            assertEquals(notes, (result as NotesUiState.Success).notes)
        }
    }

    @Test
    fun `onSearchQueryChange should update searchQuery state`() = runTest {
        // Arrange
        val query = "Searching"

        // Act
        viewModel.onSearchQueryChange(query)

        // Assert
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `deleteNote should call repository delete`() = runTest {
        // Arrange
        val noteId = 1L

        // Act
        viewModel.deleteNote(noteId)
        advanceUntilIdle()

        // Assert
        coVerify { repository.deleteNote(noteId) }
    }

    @Test
    fun `toggleFavorite should call repository with negated value`() = runTest {
        // Arrange
        val id = 1L
        val currentFav = false

        // Act
        viewModel.toggleFavorite(id, currentFav)
        advanceUntilIdle()

        // Assert
        coVerify { repository.toggleFavorite(id, true) }
    }

    @Test
    fun `summarizeNote should update aiState to Success`() = runTest {
        // Arrange
        val content = "Some content"
        val summary = "Summary"
        every { geminiService.summarizeNoteStream(content) } returns flowOf(summary)

        // Act & Assert (Flow Test with Turbine)
        viewModel.aiState.test {
            assertEquals(AiUiState.Idle, awaitItem())
            viewModel.summarizeNote(content)
            assertEquals(AiUiState.Loading, awaitItem())
            
            // Initial empty streaming state from onStart
            val streamingEmpty = awaitItem()
            assertTrue(streamingEmpty is AiUiState.Streaming)
            assertEquals("", (streamingEmpty as AiUiState.Streaming).partialResponse)
            
            // Streaming state with content
            val streamingContent = awaitItem()
            assertTrue(streamingContent is AiUiState.Streaming)
            assertEquals(summary, (streamingContent as AiUiState.Streaming).partialResponse)
            
            // Final success state
            val result = awaitItem()
            assertTrue(result is AiUiState.Success)
            assertEquals(summary, (result as AiUiState.Success).response)
        }
    }
}
