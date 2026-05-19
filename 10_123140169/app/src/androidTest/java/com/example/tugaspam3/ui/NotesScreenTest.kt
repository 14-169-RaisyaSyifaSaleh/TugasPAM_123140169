package com.example.tugaspam3.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.tugaspam3.data.GeminiService
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.platform.NetworkMonitor
import com.example.tugaspam3.viewmodel.NotesViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository = mockk<NoteRepository>(relaxed = true)
    private val networkMonitor = mockk<NetworkMonitor>(relaxed = true)
    private val geminiService = mockk<GeminiService>(relaxed = true)
    private lateinit var viewModel: NotesViewModel

    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())

    @Before
    fun setup() {
        every { networkMonitor.isConnected } returns flowOf(true)
        every { repository.getAllNotes() } returns notesFlow
        
        viewModel = NotesViewModel(repository, networkMonitor, geminiService)
    }

    @Test
    fun appTitle_isDisplayed() {
        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, onNoteClick = {})
        }

        composeTestRule.onNodeWithText("Notes App").assertIsDisplayed()
    }

    @Test
    fun emptyState_showsMessage() {
        notesFlow.value = emptyList()

        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, onNoteClick = {})
        }

        composeTestRule.onNodeWithText("Your story begins here...").assertIsDisplayed()
    }

    @Test
    fun notesList_displaysNotes() {
        val testNotes = listOf(
            Note(1, "First Note", "Content 1", 0L, false),
            Note(2, "Second Note", "Content 2", 0L, true)
        )
        notesFlow.value = testNotes

        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, onNoteClick = {})
        }

        composeTestRule.onNodeWithText("First Note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second Note").assertIsDisplayed()
    }
}
