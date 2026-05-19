package com.example.tugaspam3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugaspam3.model.NotesUiState
import com.example.tugaspam3.ui.theme.*
import com.example.tugaspam3.viewmodel.NotesViewModel

@Composable
fun FavoritesScreen(
    viewModel: NotesViewModel,
    onNoteClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = LocalIsDark.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background Glow
        if (isDark) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 100.dp, y = (-50).dp)
                    .blur(100.dp)
                    .background(SoftPink.copy(alpha = 0.1f), CircleShape)
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                ),
                modifier = Modifier.padding(start = 24.dp, top = 64.dp, end = 24.dp, bottom = 24.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            when (val state = uiState) {
                is NotesUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = SoftPink)
                    }
                }
                is NotesUiState.Empty, is NotesUiState.Success -> {
                    val notes = (state as? NotesUiState.Success)?.notes ?: emptyList()
                    val favoriteNotes = notes.filter { it.isFavorite }

                    if (favoriteNotes.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "No favorites yet. Tap the heart!",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(favoriteNotes) { note ->
                                GlassNoteItem(
                                    note = note,
                                    isDark = isDark,
                                    onClick = { onNoteClick(note.id) },
                                    onFavoriteToggle = { viewModel.toggleFavorite(note.id, note.isFavorite) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
