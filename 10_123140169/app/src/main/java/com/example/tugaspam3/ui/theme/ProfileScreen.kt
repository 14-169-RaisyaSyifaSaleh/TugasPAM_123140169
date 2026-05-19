package com.example.tugaspam3.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugaspam3.ui.theme.*
import com.example.tugaspam3.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val isDark = LocalIsDark.current

    // Macho Industrial Background
    val backgroundBrush = Brush.verticalGradient(
        listOf(
            if (isDark) Color(0xFF0F172A) else Color(0xFFE2E8F0),
            if (isDark) Color(0xFF020617) else Color(0xFF94A3B8)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .drawBehind {
                // Macho Diagonal Pattern
                val strokeWidth = 1.dp.toPx()
                val spacing = 24.dp.toPx()
                val color = if (isDark) Color.White.copy(alpha = 0.03f) else Color.Black.copy(alpha = 0.04f)
                var x = -size.width
                while (x < size.width + size.height) {
                    drawLine(
                        color = color,
                        start = Offset(x, 0f),
                        end = Offset(x + size.height, size.height),
                        strokeWidth = strokeWidth
                    )
                    x += spacing
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Hero Section: Profile Image
            Box(contentAlignment = Alignment.TopCenter) {
                ProfileHeader(imageUri = state.profileImageUri)
            }

            Spacer(modifier = Modifier.height(70.dp))

            // 2. Identity Section
            Text(
                text = state.name.uppercase(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                ),
                color = if (isDark) Color.White else Color(0xFF1E293B),
                textAlign = TextAlign.Center
            )
            
            Surface(
                color = ElectricBlue,
                shape = RoundedCornerShape(2.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = state.nim,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Bio Section (Rugged Design)
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .background(if (isDark) Color(0xFF1E293B) else Color.White)
                    .border(2.dp, if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1))
                    .padding(20.dp)
            ) {
                Text(
                    text = state.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color.White.copy(alpha = 0.8f) else Color(0xFF475569),
                    textAlign = TextAlign.Start,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. Info Section (Grid-like Industrial Tiles)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileInfoTile(Icons.Default.Email, "EMAIL", state.email, ElectricBlue, isDark)
                ProfileInfoTile(Icons.Default.Phone, "PHONE", state.phone, SoftPurple, isDark)
                ProfileInfoTile(Icons.Default.LocationOn, "LOCATION", state.location, SoftPink, isDark)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 5. Action Row (Solid Blocky Buttons)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f).height(54.dp),
                    shape = RoundedCornerShape(0.dp), // Squared corners for macho look
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                        contentColor = if (isDark) Color.White else Color.Black
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EDIT", fontWeight = FontWeight.ExtraBold)
                }

                Button(
                    onClick = onSettingsClick,
                    modifier = Modifier.weight(1f).height(54.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SETTINGS", fontWeight = FontWeight.ExtraBold)
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun ProfileInfoTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    isDark: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDark) Color(0xFF1E293B).copy(alpha = 0.5f) else Color.White)
            .border(1.dp, if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon, 
            contentDescription = null, 
            tint = accentColor, 
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) Color.White else Color(0xFF1E293B)
            )
        }
    }
}
