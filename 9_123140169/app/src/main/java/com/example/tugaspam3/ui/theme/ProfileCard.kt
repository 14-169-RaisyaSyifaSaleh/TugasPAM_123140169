package com.example.tugaspam3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugaspam3.model.ProfileUiState
import com.example.tugaspam3.ui.theme.*

@Composable
fun ProfileCard(state: ProfileUiState) {
    val isDark = LocalIsDark.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name and NIM Section
        Text(
            text = state.name,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            ),
            color = if (isDark) Color.White else SlateDark,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = state.nim,
            style = MaterialTheme.typography.titleMedium,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bio Section (Glassy Badge)
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (isDark) GlassWhite else GlassBlack.copy(alpha = 0.05f))
                .border(1.dp, if (isDark) GlassBorder else LightBorder, RoundedCornerShape(16.dp)),
            color = Color.Transparent
        ) {
            Text(
                text = state.bio,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color.White.copy(alpha = 0.8f) else SlateLight,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        // Info Section (Glassy Container)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(if (isDark) GlassWhite else GlassBlack.copy(alpha = 0.03f))
                .border(1.dp, if (isDark) GlassBorder else LightBorder, RoundedCornerShape(32.dp))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                InfoItem(
                    icon = Icons.Default.Email,
                    title = "Email Address",
                    value = state.email,
                    iconColor = ElectricBlue,
                    isDark = isDark
                )
                InfoItem(
                    icon = Icons.Default.Phone,
                    title = "Phone Number",
                    value = state.phone,
                    iconColor = SoftPurple,
                    isDark = isDark
                )
                InfoItem(
                    icon = Icons.Default.LocationOn,
                    title = "Current Location",
                    value = state.location,
                    iconColor = SoftPink,
                    isDark = isDark
                )
            }
        }
    }
}
