package com.example.tugaspam3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sort
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

/**
 * Settings Screen of the application.
 * Displays application preferences and platform-specific device information.
 * Integrated with Koin DI for ViewModel and Platform Features.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val isDark = LocalIsDark.current
    val deviceInfo = viewModel.deviceInfo
    val batteryInfo = viewModel.batteryInfo

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
                            "Settings",
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
                // Appearance Section
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
                                "Appearance",
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

                // Device & System Info Section (Platform Features & Bonus)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isDark) MidnightBlue else IceBlue)
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "System Information",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        DeviceInfoItem("Manufacturer", deviceInfo.manufacturer, isDark)
                        DeviceInfoItem("Model", deviceInfo.model, isDark)
                        DeviceInfoItem("OS Version", "Android ${deviceInfo.osVersion}", isDark)
                        
                        // Battery Info (Bonus Point)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (batteryInfo.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                                    contentDescription = null,
                                    tint = if (isDark) Color.White.copy(alpha = 0.6f) else Slate500,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Battery", color = (if (isDark) Color.White else Slate900).copy(alpha = 0.6f), style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(
                                "${batteryInfo.level}% ${if (batteryInfo.isCharging) "(Charging)" else ""}",
                                color = AuroraGreen,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Sort Order Section
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 4.dp)) {
                        Icon(Icons.Default.Sort, contentDescription = null, tint = ElectricViolet, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Sorting",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricViolet,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDark) MidnightBlue else IceBlue)
                            .padding(8.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            SortOrder.entries.forEach { order ->
                                val isSelected = sortOrder == order
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) (if (isDark) DeepSpace else Color.White) 
                                            else Color.Transparent
                                        )
                                        .clickable { viewModel.updateSortOrder(order) }
                                        .padding(horizontal = 12.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.updateSortOrder(order) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = CyberCyan,
                                            unselectedColor = if (isDark) Color.White.copy(alpha = 0.5f) else Slate500
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = when(order) {
                                            SortOrder.BY_TITLE -> "Alphabetical"
                                            SortOrder.BY_DATE -> "Date Created"
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = when {
                                            isSelected -> if (isDark) CyberCyan else Slate900
                                            else -> if (isDark) Color.White else Slate700
                                        },
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    "Version 1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = (if (isDark) Color.White else Slate900).copy(alpha = 0.3f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

/**
 * Reusable component for displaying device information items.
 */
@Composable
fun DeviceInfoItem(label: String, value: String, isDark: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = (if (isDark) Color.White else Slate900).copy(alpha = 0.6f), style = MaterialTheme.typography.bodyMedium)
        Text(value, color = if (isDark) Color.White else Slate900, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
    }
}
