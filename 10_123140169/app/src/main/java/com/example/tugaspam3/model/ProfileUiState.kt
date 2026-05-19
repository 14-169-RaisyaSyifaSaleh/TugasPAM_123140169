package com.example.tugaspam3.model

import android.net.Uri

data class ProfileUiState(
    val name: String = "",
    val nim: String = "",
    val bio: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val profileImageUri: Uri? = null,
    val isDarkMode: Boolean = false
)
