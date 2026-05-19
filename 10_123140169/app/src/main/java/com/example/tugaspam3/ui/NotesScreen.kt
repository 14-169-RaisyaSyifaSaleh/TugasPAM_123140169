package com.example.tugaspam3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.tugaspam3.model.Note
import com.example.tugaspam3.model.NotesUiState
import com.example.tugaspam3.ui.theme.*
import com.example.tugaspam3.viewmodel.NotesViewModel

@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    onNoteClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isDark = LocalIsDark.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background Glows (Only for Dark Mode)
        if (isDark) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(x = (-100).dp, y = (-50).dp)
                    .blur(100.dp)
                    .background(ElectricBlue.copy(alpha = 0.15f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 50.dp, y = 50.dp)
                    .blur(80.dp)
                    .background(SoftPurple.copy(alpha = 0.15f), CircleShape)
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Notes App",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                ),
                modifier = Modifier.padding(start = 24.dp, top = 64.dp, end = 24.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
                GlassSearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) }
                )

                // Search Results Overlay
                if (searchQuery.isNotEmpty()) {
                    val searchResults = (uiState as? NotesUiState.Success)?.notes ?: emptyList()
                    
                    if (searchResults.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .offset(y = 88.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isDark) DeepMidnight.copy(alpha = 0.95f) 
                                        else Color.White.copy(alpha = 0.98f)
                                    )
                                    .border(
                                        1.dp, 
                                        if (isDark) GlassBorder else LightBorder, 
                                        RoundedCornerShape(24.dp)
                                    )
                                    .heightIn(max = 300.dp)
                            ) {
                                LazyColumn(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    items(searchResults) { note ->
                                        SearchItem(
                                            note = note,
                                            isDark = isDark,
                                            onClick = { 
                                                viewModel.onSearchQueryChange("")
                                                onNoteClick(note.id) 
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Main Content
            if (searchQuery.isEmpty()) {
                when (val state = uiState) {
                    is NotesUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = ElectricBlue)
                        }
                    }
                    is NotesUiState.Empty -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "Your story begins here...", 
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }
                    }
                    is NotesUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.notes) { note ->
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

@Composable
fun SearchItem(
    note: Note,
    isDark: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ElectricBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Search, 
                contentDescription = null, 
                tint = ElectricBlue, 
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                note.title, 
                color = if (isDark) Color.White else SlateDark, 
                fontWeight = FontWeight.Bold, 
                maxLines = 1, 
                overflow = TextOverflow.Ellipsis
            )
            Text(
                note.content, 
                color = if (isDark) Color.White.copy(alpha = 0.8f) else SlateLight, 
                style = MaterialTheme.typography.bodySmall, 
                maxLines = 1, 
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GlassSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    val isDark = LocalIsDark.current
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isDark) GlassWhite else GlassBlack.copy(alpha = 0.05f))
            .border(1.dp, if (isDark) GlassBorder else LightBorder, RoundedCornerShape(24.dp)),
        placeholder = { 
            Text(
                "Search your thoughts...", 
                color = (if (isDark) Color.White else SlateDark).copy(alpha = 0.6f)
            ) 
        },
        leadingIcon = { 
            Icon(
                Icons.Default.Search, 
                contentDescription = null, 
                tint = if (isDark) Color.White else SlateDark 
            ) 
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = if (isDark) Color.White else ElectricBlue,
            focusedTextColor = if (isDark) Color.White else SlateDark,
            unfocusedTextColor = if (isDark) Color.White else SlateDark
        ),
        singleLine = true
    )
}

@Composable
fun GlassNoteItem(
    note: Note,
    isDark: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(if (isDark) GlassWhite else GlassBlack.copy(alpha = 0.05f))
            .border(1.dp, if (isDark) GlassBorder else LightBorder, RoundedCornerShape(28.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(ElectricBlue.copy(alpha = 0.8f), SoftPurple.copy(alpha = 0.8f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = note.title.take(1).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = if (isDark) Color.White else SlateDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color.White.copy(alpha = 0.8f) else SlateLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = onFavoriteToggle,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (note.isFavorite) ElectricBlue.copy(alpha = 0.1f)
                        else Color.Transparent
                    )
            ) {
                Icon(
                    imageVector = if (note.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (note.isFavorite) (if (isDark) SoftPink else ElectricBlue) else (if (isDark) Color.White.copy(alpha = 0.6f) else SlateLight.copy(alpha = 0.6f))
                )
            }
        }
    }
}
