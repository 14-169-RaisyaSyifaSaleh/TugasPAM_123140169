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
import com.example.a3_123140169.ui.theme._3_123140169Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _3_123140169Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F9FA)
                ) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val name = "Raisya Syifa Saleh"
    val nim = "123140169"
    val bio = "Mahasiswi S1 Teknik Informatika Institut Teknologi Sumatera"
    val email = "raisya.123140169@student.itera.ac.id"
    val phone = "+62 878-6746-3267"
    val location = "Bandar Lampung, Indonesia"

    val scrollState = rememberScrollState()
    
    // Light Mesh Background
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE9EEF5), 
            Color(0xFFF8F9FA),
            Color(0xFFF0F4F8)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFF81ECEC).copy(0.25f), Color.Transparent)),
                radius = 800f,
                center = Offset(size.width * 0.1f, size.height * 0.2f)
            )
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFFFAB1A0).copy(0.2f), Color.Transparent)),
                radius = 900f,
                center = Offset(size.width * 0.9f, size.height * 0.8f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Profile Header
            ProfileHeader(name, nim)

            Spacer(modifier = Modifier.height(40.dp))

            LiquidGlassCard {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = bio,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF2D3436),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    InfoItem(Icons.Rounded.Email, "Email", email, Color(0xFF0984E3))
                    InfoItem(Icons.Rounded.Phone, "Phone", phone, Color(0xFFD63031))
                    InfoItem(Icons.Rounded.LocationOn, "Location", location, Color(0xFF00B894))
                    InfoItem(Icons.Rounded.Info, "GitHub", "github.com/raisya123", Color(0xFF6C5CE7))
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun ProfileHeader(name: String, nim: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF74B9FF).copy(0.3f), Color.Transparent)
                        ),
                        radius = size.maxDimension * 0.7f
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(4.dp, Color.White, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color(0xFF74B9FF).copy(alpha = 0.3f), CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2D3436),
                letterSpacing = 0.5.sp
            ),
            textAlign = TextAlign.Center
        )
        
        Text(
            text = nim,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color(0xFF636E72),
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
        
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(4.dp)
                .width(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color(0xFFFD79A8), Color(0xFFE84393))),
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color(0xFFF1F2F6), RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(accentColor.copy(0.15f), RoundedCornerShape(12.dp))
                .border(1.dp, accentColor.copy(0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF636E72),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF2D3436),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LiquidGlassCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.8f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(Color.White, Color(0xFFDFE6E9).copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .drawBehind {
                    val spacing = 24.dp.toPx()
                    for (x in 0..(size.width / spacing).toInt()) {
                        for (y in 0..(size.height / spacing).toInt()) {
                            drawCircle(
                                color = Color.Gray.copy(alpha = 0.05f),
                                radius = 1.dp.toPx(),
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
        Surface(color = Color(0xFFF8F9FA)) {
            ProfileScreen()
        }
    }
}
