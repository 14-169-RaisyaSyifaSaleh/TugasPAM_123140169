package com.example.a1_123140169

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1_123140169.ui.theme._1_123140169Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _1_123140169Theme {
                // Menggunakan Box sebagai root dengan fillMaxSize agar background memenuhi layar
                // dan contentAlignment = Alignment.Center agar kartu benar-benar di tengah.
                val gradient = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6200EE), // Deep Purple
                        Color(0xFF03DAC5)  // Teal
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = gradient),
                    contentAlignment = Alignment.Center
                ) {
                    GreetingCard(
                        name = "Raisya Syifa Saleh",
                        nim = "123140169",
                        platform = "Android"
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingCard(name: String, nim: String, platform: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
            .widthIn(max = 400.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Halo,",
                fontSize = 18.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Text(
                text = name,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF6200EE),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "NIM: $nim",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Platform",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = platform,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF018786)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    _1_123140169Theme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            GreetingCard("Raisya Syifa Saleh", "123140169", "Android")
        }
    }
}
