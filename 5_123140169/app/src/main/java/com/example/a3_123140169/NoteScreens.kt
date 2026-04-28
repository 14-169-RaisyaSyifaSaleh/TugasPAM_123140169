package com.example.a3_123140169

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NotesViewModel,
    profileViewModel: ProfileViewModel,
    onNoteClick: (Int) -> Unit,
    onAddNoteClick: () -> Unit,
    onlyFavorites: Boolean = false
) {
    val notes by viewModel.notes.collectAsState()
    val uiState by profileViewModel.uiState.collectAsState()
    val filteredNotes = if (onlyFavorites) notes.filter { it.isFavorite } else notes

    val backgroundBrush = if (uiState.isDarkMode) {
        Brush.verticalGradient(colors = listOf(Color(0xFF0F172A), Color(0xFF020617)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0)))
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(if (uiState.isDarkMode) Color(0xFF38BDFC).copy(0.1f) else Color(0xFF38BDFC).copy(0.15f), Color.Transparent)
                ),
                radius = 800f,
                center = Offset(size.width * 0.1f, size.height * 0.2f)
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            if (onlyFavorites) "Saved Favorites" else "My Notes",
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp,
                            color = if (uiState.isDarkMode) Color.White else Color(0xFF0F172A)
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            floatingActionButton = {
                if (!onlyFavorites) {
                    FloatingActionButton(
                        onClick = onAddNoteClick,
                        containerColor = Color(0xFF38BDFC),
                        contentColor = Color.White,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = "Add Note", modifier = Modifier.size(28.dp))
                    }
                }
            }
        ) { padding ->
            if (filteredNotes.isEmpty()) {
                EmptyState(onlyFavorites, uiState.isDarkMode, padding)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredNotes) { note ->
                        NoteCard(note, uiState.isDarkMode, onNoteClick, { viewModel.toggleFavorite(note.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    isDark: Boolean,
    onNoteClick: (Int) -> Unit,
    onToggleFavorite: () -> Unit
) {
    LiquidGlassCard(
        isDark = isDark,
        modifier = Modifier.clickable { onNoteClick(note.id) }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isDark) Color.White else Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = note.content,
                    fontSize = 14.sp,
                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (note.isFavorite) Color(0xFFF43F5E).copy(0.1f) 
                        else if (isDark) Color.White.copy(0.05f) else Color.Black.copy(0.05f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (note.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (note.isFavorite) Color(0xFFF43F5E) else if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyState(onlyFavorites: Boolean, isDark: Boolean, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .blur(40.dp)
                    .background(
                        Color(0xFF38BDFC).copy(alpha = 0.2f),
                        CircleShape
                    )
            )
            Icon(
                imageVector = if (onlyFavorites) Icons.Rounded.Bookmarks else Icons.Rounded.Description,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = if (isDark) Color(0xFF38BDFC).copy(0.4f) else Color(0xFF38BDFC).copy(0.6f)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = if (onlyFavorites) "No Favorites Yet" else "Your Notes are Empty",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (onlyFavorites) "Save your favorite notes to see them here." else "Start writing your thoughts and ideas today!",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Int,
    viewModel: NotesViewModel,
    profileViewModel: ProfileViewModel,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val uiState by profileViewModel.uiState.collectAsState()
    val note = notes.find { it.id == noteId }
    val isDark = uiState.isDarkMode

    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(colors = listOf(Color(0xFF0F172A), Color(0xFF020617)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0)))
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Note Details", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEdit(noteId) }) {
                            Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = Color(0xFF38BDFC))
                        }
                        IconButton(onClick = { 
                            viewModel.deleteNote(noteId)
                            onBack()
                        }) {
                            Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color(0xFFF43F5E))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            note?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) Color.White else Color(0xFF0F172A),
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .width(40.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF38BDFC))
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = it.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                        lineHeight = 28.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    noteId: Int? = null,
    viewModel: NotesViewModel,
    profileViewModel: ProfileViewModel,
    onBack: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val uiState by profileViewModel.uiState.collectAsState()
    val isDark = uiState.isDarkMode
    val existingNote = noteId?.let { id -> notes.find { it.id == id } }

    var title by remember { mutableStateOf(existingNote?.title ?: "") }
    var content by remember { mutableStateOf(existingNote?.content ?: "") }

    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(colors = listOf(Color(0xFF0F172A), Color(0xFF020617)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0)))
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(if (noteId == null) "New Note" else "Edit Note", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF38BDFC),
                        unfocusedBorderColor = if (isDark) Color.White.copy(0.1f) else Color.Black.copy(0.1f)
                    ),
                    textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Write something...", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF38BDFC),
                        unfocusedBorderColor = if (isDark) Color.White.copy(0.1f) else Color.Black.copy(0.1f)
                    )
                )
                
                Button(
                    onClick = {
                        if (noteId == null) {
                            viewModel.addNote(title, content)
                        } else {
                            viewModel.updateNote(noteId, title, content)
                        }
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDFC)),
                    enabled = title.isNotBlank() && content.isNotBlank()
                ) {
                    Text("Save Note", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
