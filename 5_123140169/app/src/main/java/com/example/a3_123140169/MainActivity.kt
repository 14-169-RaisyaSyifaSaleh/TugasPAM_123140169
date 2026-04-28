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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.a3_123140169.ui.theme._3_123140169Theme

sealed class Screen(val route: String, val title: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    object Notes : Screen("notes", "Notes", Icons.Rounded.Description, Icons.Rounded.Description)
    object Favorites : Screen("favorites", "Favorites", Icons.Rounded.Bookmarks, Icons.Rounded.Bookmarks)
    object Profile : Screen("profile", "Profile", Icons.Rounded.AccountCircle, Icons.Rounded.AccountCircle)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profileViewModel: ProfileViewModel = viewModel()
            val notesViewModel: NotesViewModel = viewModel()
            val uiState by profileViewModel.uiState.collectAsState()

            _3_123140169Theme(darkTheme = uiState.isDarkMode) {
                MainApp(profileViewModel, notesViewModel)
            }
        }
    }
}

@Composable
fun MainApp(profileViewModel: ProfileViewModel, notesViewModel: NotesViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val uiState by profileViewModel.uiState.collectAsState()

    val bottomBarScreens = listOf(Screen.Notes, Screen.Favorites, Screen.Profile)
    val showBottomBar = bottomBarScreens.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = if (uiState.isDarkMode) Color(0xFF0F172A).copy(0.95f) else Color.White.copy(0.95f),
                    tonalElevation = 0.dp,
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    bottomBarScreens.forEach { screen ->
                        val selected = currentDestination?.route == screen.route
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    imageVector = if (selected) screen.selectedIcon else screen.icon, 
                                    contentDescription = null,
                                    modifier = Modifier.size(26.dp)
                                ) 
                            },
                            label = { 
                                Text(
                                    screen.title, 
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                ) 
                            },
                            selected = selected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF38BDFC),
                                selectedTextColor = Color(0xFF38BDFC),
                                indicatorColor = Color(0xFF38BDFC).copy(alpha = 0.1f),
                                unselectedIconColor = if (uiState.isDarkMode) Color(0xFF64748B) else Color(0xFF94A3B8),
                                unselectedTextColor = if (uiState.isDarkMode) Color(0xFF64748B) else Color(0xFF94A3B8)
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Notes.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Notes.route) {
                NoteListScreen(
                    viewModel = notesViewModel,
                    profileViewModel = profileViewModel,
                    onNoteClick = { id -> navController.navigate("noteDetail/$id") },
                    onAddNoteClick = { navController.navigate("addNote") }
                )
            }
            composable(Screen.Favorites.route) {
                NoteListScreen(
                    viewModel = notesViewModel,
                    profileViewModel = profileViewModel,
                    onNoteClick = { id -> navController.navigate("noteDetail/$id") },
                    onAddNoteClick = {},
                    onlyFavorites = true
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel = profileViewModel)
            }
            composable(
                route = "noteDetail/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
                NoteDetailScreen(
                    noteId = noteId,
                    viewModel = notesViewModel,
                    profileViewModel = profileViewModel,
                    onBack = { navController.popBackStack() },
                    onEdit = { id -> navController.navigate("editNote/$id") }
                )
            }
            composable("addNote") {
                AddEditNoteScreen(
                    viewModel = notesViewModel,
                    profileViewModel = profileViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "editNote/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
                AddEditNoteScreen(
                    noteId = noteId,
                    viewModel = notesViewModel,
                    profileViewModel = profileViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var isEditing by remember { mutableStateOf(false) }
    
    var editName by remember(uiState.name) { mutableStateOf(uiState.name) }
    var editBio by remember(uiState.bio) { mutableStateOf(uiState.bio) }

    val backgroundBrush = if (uiState.isDarkMode) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF0F172A), Color(0xFF020617))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0))
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
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
                            shape = RoundedCornerShape(16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = editBio,
                            onValueChange = { editBio = it },
                            label = { Text("Bio") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
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
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                        .background(if (isDark) Color(0xFF1E272E) else Color(0xFFDFE6ED)),
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
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    isDark: Boolean, 
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
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
        content()
    }
}
