package com.example.tugaspam3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.tugaspam3.ui.*
import com.example.tugaspam3.ui.theme.*
import com.example.tugaspam3.viewmodel.NotesViewModel
import com.example.tugaspam3.viewmodel.ProfileViewModel
import com.example.tugaspam3.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsViewModel: SettingsViewModel = koinViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            val notesViewModel: NotesViewModel = koinViewModel()
            val profileViewModel: ProfileViewModel = koinViewModel()

            TugasPAM3Theme(darkTheme = isDarkMode) {
                MainApp(profileViewModel, notesViewModel, settingsViewModel)
            }
        }
    }
}

@Composable
fun MainApp(
    profileViewModel: ProfileViewModel,
    notesViewModel: NotesViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isDark = LocalIsDark.current
    val isConnected by notesViewModel.isConnected.collectAsState()

    val bottomBarScreens = listOf(Screen.Notes, Screen.Favorites, Screen.Profile)
    val showBottomBar = currentDestination?.route in bottomBarScreens.map { it.route }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                if (!isConnected) {
                    Surface(
                        color = Color.Red.copy(alpha = 0.8f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No Internet Connection",
                            color = Color.White,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .border(1.dp, if (isDark) GlassBorder else LightBorder, RoundedCornerShape(32.dp)),
                            color = if (isDark) GlassWhite else GlassBlack.copy(alpha = 0.05f),
                            tonalElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                bottomBarScreens.forEach { screen ->
                                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                    
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val icon = when (screen) {
                                            Screen.Notes -> if (isSelected) Icons.Default.Description else Icons.AutoMirrored.Outlined.Notes
                                            Screen.Favorites -> if (isSelected) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder
                                            Screen.Profile -> if (isSelected) Icons.Default.Person else Icons.Outlined.Person
                                            else -> Icons.Default.List
                                        }
                                        
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize(0.7f)
                                                    .clip(CircleShape)
                                                    .background(
                                                        Brush.linearGradient(
                                                            listOf(ElectricBlue.copy(alpha = 0.3f), SoftPurple.copy(alpha = 0.3f))
                                                        )
                                                    )
                                            )
                                        }
                                        
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = if (isSelected) (if (isDark) Color.White else ElectricBlue) else (if (isDark) Color.White.copy(alpha = 0.4f) else SlateLight),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                if (currentDestination?.route == Screen.Notes.route) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.AddNote.route) },
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        modifier = Modifier
                            .padding(bottom = 90.dp)
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(ElectricBlue, SoftPurple)
                                )
                            )
                            .border(1.dp, GlassBorder, CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Note", tint = Color.White, modifier = Modifier.size(32.dp))
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
                    NotesScreen(
                        viewModel = notesViewModel,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        }
                    )
                }
                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = notesViewModel,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        viewModel = profileViewModel,
                        onEditClick = { navController.navigate(Screen.EditProfile.route) },
                        onSettingsClick = { navController.navigate(Screen.Settings.route) }
                    )
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        viewModel = settingsViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable(Screen.EditProfile.route) {
                    EditProfileScreen(
                        viewModel = profileViewModel,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getLong("noteId") ?: return@composable
                    NoteDetailScreen(
                        noteId = noteId,
                        viewModel = notesViewModel,
                        onBackClick = { navController.popBackStack() },
                        onEditClick = { id ->
                            navController.navigate(Screen.EditNote.createRoute(id))
                        },
                        onDeleteClick = { navController.popBackStack() }
                    )
                }
                composable(Screen.AddNote.route) {
                    AddEditNoteScreen(
                        viewModel = notesViewModel,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getLong("noteId") ?: return@composable
                    AddEditNoteScreen(
                        noteId = noteId,
                        viewModel = notesViewModel,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
