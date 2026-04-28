package com.example.a3_123140169

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a3_123140169.ui.theme._3_123140169Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ProfileViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            _3_123140169Theme(darkTheme = uiState.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var isEditing by remember { mutableStateOf(false) }
    
    // State hoisting for text fields
    var editName by remember(uiState.name) { mutableStateOf(uiState.name) }
    var editBio by remember(uiState.bio) { mutableStateOf(uiState.bio) }

    val backgroundBrush = if (uiState.isDarkMode) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F172A),
                Color(0xFF020617)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF8FAFC),
                Color(0xFFE2E8F0)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Decorative background elements for more "Liquid Glass" look
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        if (uiState.isDarkMode) Color(0xFF38BDFC).copy(0.15f) else Color(0xFF38BDFC).copy(0.2f),
                        Color.Transparent
                    )
                ),
                radius = 1000f,
                center = Offset(size.width * 0.9f, size.height * 0.1f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        if (uiState.isDarkMode) Color(0xFF818CF8).copy(0.12f) else Color(0xFF818CF8).copy(0.15f),
                        Color.Transparent
                    )
                ),
                radius = 1200f,
                center = Offset(size.width * -0.2f, size.height * 0.5f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            ProfileHeader(uiState.name, uiState.nim, uiState.isDarkMode)

            Spacer(modifier = Modifier.height(32.dp))

            LiquidGlassCard(isDark = uiState.isDarkMode) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Nama") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFF38BDFC),
                                cursorColor = Color(0xFF38BDFC)
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = editBio,
                            onValueChange = { editBio = it },
                            label = { Text("Bio") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFF38BDFC),
                                cursorColor = Color(0xFF38BDFC)
                            )
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                viewModel.updateName(editName)
                                viewModel.updateBio(editBio)
                                isEditing = false
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text("Simpan Perubahan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        TextButton(onClick = { isEditing = false }, modifier = Modifier.padding(top = 8.dp)) {
                            Text("Batal", color = Color(0xFFEF4444), fontWeight = FontWeight.Medium)
                        }
                    } else {
                        Text(
                            text = uiState.bio,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (uiState.isDarkMode) Color(0xFFCBD5E1) else Color(0xFF475569),
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDFC))
                        ) {
                            Icon(Icons.Rounded.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Edit Profil", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    InfoItem(Icons.Rounded.Email, "Email", uiState.email, Color(0xFF38BDFC), uiState.isDarkMode)
                    InfoItem(Icons.Rounded.Phone, "Phone", uiState.phone, Color(0xFFF43F5E), uiState.isDarkMode)
                    InfoItem(Icons.Rounded.LocationOn, "Location", uiState.location, Color(0xFF10B981), uiState.isDarkMode)
                    InfoItem(Icons.Rounded.Code, "GitHub", "github.com/raisya123", Color(0xFF818CF8), uiState.isDarkMode)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Dark Mode Toggle at the bottom with improved design
            LiquidGlassCard(isDark = uiState.isDarkMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (uiState.isDarkMode) Color(0xFFF59E0B).copy(0.15f) else Color(0xFF6366F1).copy(0.15f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (uiState.isDarkMode) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                                contentDescription = null,
                                tint = if (uiState.isDarkMode) Color(0xFFF59E0B) else Color(0xFF6366F1),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = if (uiState.isDarkMode) "Light Mode" else "Dark Mode",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.isDarkMode) Color.White else Color(0xFF1E293B)
                        )
                    }
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF38BDFC),
                            uncheckedThumbColor = Color(0xFF94A3B8),
                            uncheckedTrackColor = Color(0xFFE2E8F0)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun ProfileHeader(name: String, nim: String, isDark: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            // Glow effect behind avatar
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .blur(30.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF38BDFC).copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
            
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                if (isDark) Color(0xFF1E293B) else Color.White,
                                if (isDark) Color(0xFF0F172A) else Color(0xFFF1F5F9)
                            )
                        )
                    )
                    .border(
                        2.dp, 
                        Brush.linearGradient(
                            listOf(Color(0xFF38BDFC), Color(0xFF818CF8))
                        ), 
                        CircleShape
                    )
                    .padding(6.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) Color.White else Color(0xFF0F172A),
                letterSpacing = (-0.5).sp
            ),
            textAlign = TextAlign.Center
        )
        
        Text(
            text = nim,
            style = MaterialTheme.typography.titleMedium.copy(
                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .height(4.dp)
                .width(40.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    brush = Brush.horizontalGradient(listOf(Color(0xFF38BDFC), Color(0xFF818CF8)))
                )
        )
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    isDark: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isDark) Color.White.copy(0.03f) else Color.Black.copy(0.02f)
            )
            .border(
                1.dp,
                if (isDark) Color.White.copy(0.05f) else Color.Black.copy(0.03f),
                RoundedCornerShape(20.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(accentColor.copy(0.1f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color.White else Color(0xFF1E293B),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LiquidGlassCard(isDark: Boolean, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.verticalGradient(
                    colors = if (isDark) {
                        listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.02f)
                        )
                    } else {
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.6f)
                        )
                    }
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        if (isDark) Color.White.copy(0.15f) else Color.White,
                        if (isDark) Color.White.copy(0.02f) else Color.White.copy(0.2f)
                    )
                ),
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        // Grid pattern for "glass" texture
        Box(
            modifier = Modifier.fillMaxWidth()
                .drawBehind {
                    val spacing = 20.dp.toPx()
                    for (x in 0..(size.width / spacing).toInt()) {
                        for (y in 0..(size.height / spacing).toInt()) {
                            drawCircle(
                                color = if (isDark) Color.White.copy(alpha = 0.04f) else Color.Black.copy(alpha = 0.02f),
                                radius = 0.8.dp.toPx(),
                                center = Offset(x * spacing, y * spacing)
                            )
                        }
                    }
                }
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    _3_123140169Theme {
        ProfileScreen()
    }
}
