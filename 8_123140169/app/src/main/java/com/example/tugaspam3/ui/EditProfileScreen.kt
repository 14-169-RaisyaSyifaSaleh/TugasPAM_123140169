package com.example.tugaspam3.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugaspam3.ui.theme.*
import com.example.tugaspam3.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val isDark = LocalIsDark.current
    
    var name by remember { mutableStateOf(state.name) }
    var nim by remember { mutableStateOf(state.nim) }
    var bio by remember { mutableStateOf(state.bio) }
    var email by remember { mutableStateOf(state.email) }
    var phone by remember { mutableStateOf(state.phone) }
    var location by remember { mutableStateOf(state.location) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(state.profileImageUri) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> if (uri != null) selectedImageUri = uri }
    )

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
                            "Edit Profile",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.updateProfile(name, nim, bio, email, phone, location)
                                viewModel.updateProfileImage(selectedImageUri)
                                onSaveClick()
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AuroraGreen)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Save", tint = DeepSpace)
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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Keep the slanted header but allow interaction
                ProfileHeader(
                    imageUri = selectedImageUri,
                    onImageClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    CyberTextField(value = name, onValueChange = { name = it }, label = "Full Name", color = AuroraGreen, isDark = isDark)
                    CyberTextField(value = nim, onValueChange = { nim = it }, label = "NIM", color = CyberCyan, isDark = isDark)
                    CyberTextField(value = bio, onValueChange = { bio = it }, label = "Bio", color = ElectricViolet, isDark = isDark, singleLine = false)
                    
                    Divider(color = if (isDark) Color.White.copy(alpha = 0.05f) else IceBlue, thickness = 1.dp)
                    
                    CyberTextField(value = email, onValueChange = { email = it }, label = "Email", color = CyberCyan, isDark = isDark)
                    CyberTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number", color = ElectricViolet, isDark = isDark)
                    CyberTextField(value = location, onValueChange = { location = it }, label = "Location", color = NeonPink, isDark = isDark)
                    
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun CyberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    color: Color,
    isDark: Boolean,
    singleLine: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(if (isDark) MidnightBlue else IceBlue)
                .border(1.dp, if (isDark) Color.White.copy(alpha = 0.05f) else Color.Transparent, RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = color,
                focusedTextColor = if (isDark) Color.White else Slate900,
                unfocusedTextColor = if (isDark) Color.White else Slate900
            ),
            singleLine = singleLine
        )
    }
}
