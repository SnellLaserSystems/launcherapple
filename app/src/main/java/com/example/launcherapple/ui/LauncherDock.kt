package com.example.launcherapple.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.launcherapple.data.AppInfo
import com.example.launcherapple.ui.components.DraggableItem

@Composable
fun LauncherDock(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onAppMove: (Int, Int) -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }
    var currentDraggedIndex by remember { mutableStateOf(-1) }
    var targetIndex by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                apps.forEachIndexed { index, app ->
                    DraggableItem(
                        item = app,
                        index = index,
                        isDragging = isDragging && currentDraggedIndex == index,
                        onDragStart = { startIndex ->
                            isDragging = true
                            currentDraggedIndex = startIndex
                        },
                        onDragEnd = {
                            if (targetIndex >= 0 && currentDraggedIndex >= 0 && targetIndex != currentDraggedIndex) {
                                onAppMove(currentDraggedIndex, targetIndex)
                            }
                            isDragging = false
                            currentDraggedIndex = -1
                            targetIndex = -1
                        },
                        onDragCancel = {
                            isDragging = false
                            currentDraggedIndex = -1
                            targetIndex = -1
                        },
                        onPositionChange = { newIndex ->
                            if (newIndex < apps.size) {
                                targetIndex = newIndex
                            }
                        },
                        onClick = { onAppClick(app) },
                        onLongClick = { onAppLongClick(app) },
                        isDockItem = true
                    )
                }
            }
        }
    }
}