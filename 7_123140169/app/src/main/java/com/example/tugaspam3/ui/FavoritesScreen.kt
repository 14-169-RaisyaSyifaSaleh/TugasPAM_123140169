package com.example.tugaspam3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
        // Modern Aurora Background
        if (isDark) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NeonPink.copy(alpha = 0.05f), Color.Transparent),
                            center = androidx.compose.ui.geometry.Offset(0f, 0f),
                            radius = 1000f
                        )
                    )
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
            ) {
                Column {
                    Text(
                        text = "PRIORITY_DECRYPT",
                        style = MaterialTheme.typography.labelLarge,
                        color = NeonPink,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Favorites",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = if (isDark) Color.White else Slate900
                    )
                }
            }

            when (val state = uiState) {
                is NotesUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = NeonPink)
                    }
                }
                is NotesUiState.Empty, is NotesUiState.Success -> {
                    val notes = (state as? NotesUiState.Success)?.notes ?: emptyList()
                    val favoriteNotes = notes.filter { it.isFavorite }

                    if (favoriteNotes.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "NO FAVORED LOGS FOUND.",
                                color = (if (isDark) Color.White else SlateDark).copy(alpha = 0.3f),
                                fontWeight = FontWeight.Black
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(favoriteNotes) { note ->
                                ModernNoteItem(
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
