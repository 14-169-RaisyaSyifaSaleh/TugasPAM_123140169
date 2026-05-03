package com.example.tugaspam3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugaspam3.ui.theme.*
import com.example.tugaspam3.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    noteId: Long? = null,
    viewModel: NotesViewModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val noteFromVm by if (noteId != null) {
        viewModel.getNoteById(noteId).collectAsState(initial = null)
    } else {
        remember { mutableStateOf(null) }
    }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val isDark = LocalIsDark.current

    LaunchedEffect(noteFromVm) {
        noteFromVm?.let {
            title = it.title
            content = it.content
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Dynamic Cyber Background
        if (isDark) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(CyberCyan.copy(alpha = 0.1f), Color.Transparent)
                        )
                    )
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (noteId == null) "INITIALIZE_ENTRY" else "MODIFY_RECORD",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            ),
                            color = if (isDark) AuroraGreen else Slate900
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        val isEnabled = title.isNotBlank() && content.isNotBlank()
                        
                        IconButton(
                            onClick = {
                                if (noteId == null) {
                                    viewModel.addNote(title, content)
                                } else {
                                    viewModel.updateNote(noteId, title, content)
                                }
                                onSaveClick()
                            },
                            enabled = isEnabled,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isEnabled) AuroraGreen else (if (isDark) Color.White.copy(alpha = 0.05f) else Color.LightGray))
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Save",
                                tint = if (isEnabled) DeepSpace else (if (isDark) Color.White.copy(alpha = 0.2f) else Color.Gray)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = if (isDark) Color.White else Slate900
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Title Field
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "ENTRY_SUBJECT",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuroraGreen,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDark) MidnightBlue else IceBlue)
                            .border(1.dp, if (isDark) Color.White.copy(alpha = 0.05f) else Color.Transparent, RoundedCornerShape(12.dp)),
                        placeholder = { Text("Enter subject...", color = (if (isDark) Color.White else Slate900).copy(alpha = 0.3f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = AuroraGreen,
                            focusedTextColor = if (isDark) Color.White else Slate900,
                            unfocusedTextColor = if (isDark) Color.White else Slate900
                        ),
                        singleLine = true
                    )
                }

                // Content Field
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                    Text(
                        "RAW_DATA_INPUT",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricViolet,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDark) MidnightBlue else IceBlue)
                            .border(1.dp, if (isDark) Color.White.copy(alpha = 0.05f) else Color.Transparent, RoundedCornerShape(12.dp)),
                        placeholder = { Text("Stream your data here...", color = (if (isDark) Color.White else Slate900).copy(alpha = 0.3f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = ElectricViolet,
                            focusedTextColor = if (isDark) Color.White else Slate900,
                            unfocusedTextColor = if (isDark) Color.White else Slate900
                        )
                    )
                }
            }
        }
    }
}
