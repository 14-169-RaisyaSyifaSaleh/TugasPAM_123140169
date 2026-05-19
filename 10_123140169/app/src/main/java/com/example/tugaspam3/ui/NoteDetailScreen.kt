package com.example.tugaspam3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.tugaspam3.ui.theme.*
import com.example.tugaspam3.viewmodel.AiUiState
import com.example.tugaspam3.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Long,
    viewModel: NotesViewModel,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: () -> Unit
) {
    val note by viewModel.getNoteById(noteId).collectAsState(initial = null)
    val aiState by viewModel.aiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAiDialog by remember { mutableStateOf(false) }
    var userQuestion by remember { mutableStateOf("") }

    if (showDeleteDialog) {
        Dialog(onDismissRequest = { showDeleteDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(DeepMidnight.copy(alpha = 0.95f))
                    .border(1.dp, GlassBorder, RoundedCornerShape(32.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(BoldRed.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = BoldRed, modifier = Modifier.size(32.dp))
                    }

                    Text(
                        "Hapus Catatan?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Text(
                        "Apakah Anda yakin ingin menghapus catatan ini? Tindakan ini tidak dapat dibatalkan.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { showDeleteDialog = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Gunmetal),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Batal", color = Color.White)
                        }
                        Button(
                            onClick = {
                                showDeleteDialog = false
                                viewModel.deleteNote(noteId)
                                onDeleteClick()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = BoldRed),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Hapus", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showAiDialog) {
        AlertDialog(
            onDismissRequest = { 
                showAiDialog = false
                viewModel.resetAiState()
                userQuestion = ""
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("Asisten Catatan AI", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Response Area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp, max = 250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        when (val state = aiState) {
                            is AiUiState.Loading -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = ElectricBlue)
                                }
                            }
                            is AiUiState.Streaming -> {
                                Text(state.partialResponse, color = Color.White.copy(alpha = 0.9f))
                            }
                            is AiUiState.Success -> {
                                Text(state.response, color = Color.White.copy(alpha = 0.9f))
                            }
                            is AiUiState.Error -> {
                                Text(state.message, color = BoldRed)
                            }
                            is AiUiState.Idle -> {
                                Text("Tanyakan sesuatu tentang catatan ini atau minta ringkasan.", color = Color.White.copy(alpha = 0.5f))
                            }
                        }
                    }

                    // Quick Actions
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ElevatedButton(
                            onClick = { note?.let { viewModel.summarizeNote(it.content) } },
                            colors = ButtonDefaults.elevatedButtonColors(containerColor = GlassWhite)
                        ) {
                            Text("Ringkas", color = ElectricBlue)
                        }
                    }

                    // Chat Input
                    OutlinedTextField(
                        value = userQuestion,
                        onValueChange = { userQuestion = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { 
                            Text(
                                "Tanya AI...", 
                                color = Color.Gray,
                                fontSize = 14.sp
                            ) 
                        },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        ),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = { 
                                if (userQuestion.isNotBlank()) {
                                    note?.let { viewModel.askAi(it.content, userQuestion) }
                                }
                            }) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = ElectricBlue)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = GlassBorder,
                            cursorColor = ElectricBlue
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    showAiDialog = false
                    viewModel.resetAiState()
                    userQuestion = ""
                }) {
                    Text("Tutup", color = ElectricBlue)
                }
            },
            containerColor = DeepMidnight,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {
        // Background Glows
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.Center)
                .blur(120.dp)
                .background(SoftPurple.copy(alpha = 0.1f), CircleShape)
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Detail",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(GlassWhite)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { showAiDialog = true },
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(GlassWhite)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "AI Assistant", tint = ElectricBlue)
                        }
                        IconButton(
                            onClick = { onEditClick(noteId) },
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(GlassWhite)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ElectricBlue)
                        }
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clip(CircleShape)
                                .background(GlassWhite)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BoldRed)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            note?.let { currentNote ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = currentNote.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 36.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SoftPurple.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Note",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = SoftPurple
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(GlassWhite)
                            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                            .padding(24.dp)
                    ) {
                        Text(
                            text = currentNote.content,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 28.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
