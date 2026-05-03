package com.example.tugaspam3.ui.theme

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tugaspam3.R

@Composable
fun ProfileHeader(
    imageUri: Uri? = null,
    onImageClick: (() -> Unit)? = null
) {
    // New Modern Dynamic Header with a slant/angle
    val gradient = Brush.horizontalGradient(
        colors = PrimaryGradient
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        // Angled background shape
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .graphicsLayer {
                    clip = true
                    shape = RoundedCornerShape(bottomStart = 80.dp)
                }
                .background(gradient)
        )

        // Profile Image positioned slightly differently
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 32.dp)
                .size(130.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(6.dp)
                .then(
                    if (onImageClick != null) Modifier.clickable { onImageClick() }
                    else Modifier
                )
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(4.dp, AuroraGreen, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(4.dp, AuroraGreen, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
