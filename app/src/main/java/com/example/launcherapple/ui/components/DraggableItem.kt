package com.example.launcherapple.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.launcherapple.data.AppInfo
import kotlin.math.roundToInt

@Composable
fun DraggableItem(
    item: AppInfo,
    index: Int,
    isDragging: Boolean,
    onDragStart: (Int) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    onPositionChange: (Int) -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isDockItem: Boolean = false,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val scale by animateFloatAsState(
        targetValue = if (isDragging) 1.2f else 1f,
        label = "ScaleAnimation"
    )

    val zIndex = if (isDragging) 1f else 0f

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .scale(scale)
            .zIndex(zIndex)
            .padding(4.dp)
            .pointerInput(item) {
                detectDragGestures(
                    onDragStart = {
                        onDragStart(index)
                    },
                    onDragEnd = {
                        offsetX = 0f
                        offsetY = 0f
                        onDragEnd()
                    },
                    onDragCancel = {
                        offsetX = 0f
                        offsetY = 0f
                        onDragCancel()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y

                        // Calculate target position based on drag distance
                        // This is a simplified approach - real implementation would
                        // calculate grid positions based on touch coordinates
                        val dragDistance = Offset(offsetX, offsetY).getDistanceSquared()
                        if (dragDistance > 5000) {  // Threshold for position change
                            val targetIndex = when {
                                offsetX > 100 -> (index + 1).coerceAtMost(if (isDockItem) 3 else 19)
                                offsetX < -100 -> (index - 1).coerceAtLeast(0)
                                else -> index
                            }
                            if (targetIndex != index) {
                                onPositionChange(targetIndex)
                            }
                        }
                    }
                )
            }
    ) {
        AppIcon(
            app = item,
            onClick = onClick,
            onLongClick = onLongClick,
            isDockItem = isDockItem
        )
    }
}