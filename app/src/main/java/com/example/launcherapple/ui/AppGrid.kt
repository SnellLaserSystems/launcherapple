package com.example.launcherapple.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.launcherapple.data.AppInfo
import com.example.launcherapple.ui.components.DraggableItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppGrid(
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
            .fillMaxSize()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(apps) { index, app ->
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
                        targetIndex = newIndex
                    },
                    onClick = { onAppClick(app) },
                    onLongClick = { onAppLongClick(app) }
                )
            }
        }
    }
}