package com.example.launcherapple.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.launcherapple.data.AppInfo
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

@Composable
fun AppOpenAnimation(
    app: AppInfo,
    isVisible: Boolean,
    onAnimationComplete: () -> Unit
) {
    if (!isVisible) return

    val scale = remember { Animatable(0.2f) }
    val alpha = remember { Animatable(1f) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    LaunchedEffect(isVisible) {
        scale.animateTo(
            targetValue = 20f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 100
            )
        )
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = alpha.value * 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(screenWidth, screenHeight)
                .scale(scale.value)
                .clip(RoundedCornerShape(12.dp))
        ) {
            androidx.compose.foundation.Image(
                bitmap = app.icon.toBitmap().asImageBitmap(),
                contentDescription = "App Icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}