package com.example.a2_123140169

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a2_123140169.ui.theme._2_123140169Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// 1. Data model untuk Berita
data class News(val id: Int, val title: String, val category: String, val content: String, val timestamp: String)

// 3. Transform data menjadi format yang ditampilkan (Display model)
data class NewsDisplayItem(val id: Int, val title: String, val category: String, val timestamp: String)

class NewsViewModel : ViewModel() {
    private val _allNews = MutableStateFlow<List<News>>(emptyList())
    
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private fun getNewsFlow(): Flow<News> = flow {
        var id = 1
        val categories = listOf("Teknologi", "Olahraga", "Politik", "Hiburan")
        val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        while (true) {
            val now = sdf.format(java.util.Date())
            emit(News(id, "Berita Utama #$id: Tren Terbaru di Dunia ${categories.random()}", categories.random(), "Konten lengkap berita $id...", now))
            id++
            delay(3000)
        }
    }

    val filteredNewsDisplay: Flow<List<NewsDisplayItem>> = combine(_allNews, _selectedCategory) { news, category ->
        news.filter { category == null || it.category == category }
            .map { NewsDisplayItem(it.id, it.title, it.category, it.timestamp) }
    }

    init {
        viewModelScope.launch {
            getNewsFlow().collect { newNews ->
                _allNews.value = (listOf(newNews) + _allNews.value).take(50)
            }
        }
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun readNews(newsId: Int) {
        viewModelScope.launch {
            delay(300) 
            _readCount.value += 1
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _2_123140169Theme {
                NewsFeedScreen()
            }
        }
    }
}

@Composable
fun NewsFeedScreen(viewModel: NewsViewModel = viewModel()) {
    val newsItems by viewModel.filteredNewsDisplay.collectAsState(initial = emptyList())
    val readCount by viewModel.readCount.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    // Gradasi warna estetik (Deep Blue to Soft Purple)
    val mainGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A237E), // Indigo 900
            Color(0xFF3F51B5), // Indigo 500
            Color(0xFF7986CB)  // Indigo 300
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                HeaderSection(readCount)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CategorySelector(selectedCategory) { viewModel.setCategory(it) }
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(newsItems, key = { it.id }) { item ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically()
                        ) {
                            NewsCard(item) { viewModel.readNews(item.id) }
                        }
                    }
                    
                    if (newsItems.isEmpty()) {
                        item {
                            EmptyState()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(readCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "News Feed",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Tetap terupdate dengan berita terkini",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$readCount",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySelector(selectedCategory: String?, onCategorySelected: (String?) -> Unit) {
    val categories = listOf("Semua", "Teknologi", "Olahraga", "Politik", "Hiburan")
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { cat ->
            val isSelected = (if (cat == "Semua") null else cat) == selectedCategory
            val bgColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.1f)
            val textColor = if (isSelected) Color(0xFF1A237E) else Color.White

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onCategorySelected(if (cat == "Semua") null else cat) },
                color = bgColor,
                shape = RoundedCornerShape(20.dp),
                border = if (!isSelected) BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)) else null
            ) {
                Text(
                    text = cat,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    color = textColor,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
fun NewsCard(item: NewsDisplayItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = getCategoryColor(item.category).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.category.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = getCategoryColor(item.category),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                ),
                color = Color(0xFF1A1A1A)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Ketuk untuk baca selengkapnya",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun LazyItemScope.EmptyState() {
    Box(
        modifier = Modifier
            .fillParentMaxSize()
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Mengambil berita terbaru...",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun getCategoryColor(category: String): Color {
    return when (category) {
        "Teknologi" -> Color(0xFF00796B)
        "Olahraga" -> Color(0xFFD32F2F)
        "Politik" -> Color(0xFF1976D2)
        "Hiburan" -> Color(0xFF7B1FA2)
        else -> Color.Gray
    }
}