package com.example.tugaspam3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugaspam3.data.SortOrder
import com.example.tugaspam3.ui.theme.*
import com.example.tugaspam3.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val isDark = LocalIsDark.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isDark) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(MidnightBlue, DeepSpace)
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
                            "SYSTEM_CONFIG",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = if (isDark) CyberCyan else Slate900,
                        navigationIconContentColor = if (isDark) Color.White else Slate900
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Dark Mode Toggle Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isDark) MidnightBlue else IceBlue)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "UI_MODE",
                                style = MaterialTheme.typography.labelSmall,
                                color = AuroraGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Dark Mode",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isDark) Color.White else Slate900
                            )
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AuroraGreen,
                                checkedTrackColor = AuroraGreen.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                // Sort Order Section
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "DATA_INDEXING",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricViolet,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDark) MidnightBlue else IceBlue)
                            .padding(8.dp)
                    ) {
                        Column {
                            SortOrder.entries.forEach { order ->
                                val isSelected = sortOrder == order
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) (if (isDark) DeepSpace else Color.White) else Color.Transparent)
                                        .clickable { viewModel.updateSortOrder(order) }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.updateSortOrder(order) },
                                        colors = RadioButtonDefaults.colors(selectedColor = CyberCyan)
                                    )
                                    Text(
                                        text = when(order) {
                                            SortOrder.BY_TITLE -> "ALPHABETICAL_SORT"
                                            SortOrder.BY_DATE -> "CHRONOLOGICAL_LOG"
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isSelected) (if (isDark) CyberCyan else Slate900) else (if (isDark) Color.White.copy(alpha = 0.5f) else Slate500),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    "BUILD_VER: 2.0.4-AURORA",
                    style = MaterialTheme.typography.labelSmall,
                    color = (if (isDark) Color.White else Slate900).copy(alpha = 0.3f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
