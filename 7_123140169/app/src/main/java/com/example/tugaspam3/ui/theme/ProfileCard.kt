package com.example.tugaspam3.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugaspam3.model.ProfileUiState

@Composable
fun ProfileCard(state: ProfileUiState) {
    val isDark = LocalIsDark.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Asymmetric Name/NIM Header
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(80.dp)
                    .background(Brush.verticalGradient(listOf(AuroraGreen, CyberCyan)))
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "ID_IDENTIFIER",
                    style = MaterialTheme.typography.labelSmall,
                    color = AuroraGreen,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = state.name.uppercase(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = if (isDark) Color.White else Slate900
                )
                Text(
                    text = "SERIAL_NO: ${state.nim}",
                    style = MaterialTheme.typography.titleMedium,
                    color = CyberCyan,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bio Section (Terminal Style)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 24.dp, bottomStart = 24.dp))
                .background(if (isDark) MidnightBlue else IceBlue)
                .border(
                    width = 1.dp,
                    color = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Transparent,
                    shape = RoundedCornerShape(topEnd = 24.dp, bottomStart = 24.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "ACCESSING_MEMORIES...",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricViolet,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = state.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color.White.copy(alpha = 0.8f) else Slate700,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        
        // Grid-based Contact Info
        Text(
            "CORE_COMMUNICATIONS",
            style = MaterialTheme.typography.labelLarge,
            color = if (isDark) AuroraGreen else Slate500,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        InfoItem(
            icon = Icons.Default.Email,
            title = "UPLINK_MAIL",
            value = state.email,
            iconColor = CyberCyan,
            isDark = isDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        InfoItem(
            icon = Icons.Default.Phone,
            title = "SIGNAL_MOBILE",
            value = state.phone,
            iconColor = ElectricViolet,
            isDark = isDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        InfoItem(
            icon = Icons.Default.LocationOn,
            title = "NODE_LOCATION",
            value = state.location,
            iconColor = NeonPink,
            isDark = isDark
        )
    }
}
