package com.example.launcherapple.data

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val category: String = "Other",
    var position: Int = -1,
    val isDockApp: Boolean = false
)