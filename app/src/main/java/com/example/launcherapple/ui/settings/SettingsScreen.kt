package com.example.launcherapple.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { onBackClick() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Divider()

        // Settings content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            SettingsCategory(title = "Appearance") {
                var darkMode by remember { mutableStateOf(false) }
                SettingsToggleItem(
                    title = "Dark Mode",
                    isChecked = darkMode,
                    onCheckedChange = { darkMode = it }
                )

                Divider()

                var useSystemFont by remember { mutableStateOf(true) }
                SettingsToggleItem(
                    title = "Use System Font",
                    isChecked = useSystemFont,
                    onCheckedChange = { useSystemFont = it }
                )

                Divider()

                SettingsSliderItem(
                    title = "Icon Size",
                    value = 0.7f,
                    onValueChange = { /* Handle icon size change */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsCategory(title = "Behavior") {
                var appLibraryEnabled by remember { mutableStateOf(true) }
                SettingsToggleItem(
                    title = "App Library",
                    isChecked = appLibraryEnabled,
                    onCheckedChange = { appLibraryEnabled = it }
                )

                Divider()

                var widgetsEnabled by remember { mutableStateOf(true) }
                SettingsToggleItem(
                    title = "Widget Screen",
                    isChecked = widgetsEnabled,
                    onCheckedChange = { widgetsEnabled = it }
                )

                Divider()

                SettingsNavigationItem(
                    title = "Default Apps",
                    onClick = { /* Navigate to default apps settings */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsCategory(title = "About") {
                SettingsInfoItem(
                    title = "Version",
                    value = "1.0.0"
                )

                Divider()

                SettingsNavigationItem(
                    title = "Privacy Policy",
                    onClick = { /* Navigate to privacy policy */ }
                )

                Divider()

                SettingsNavigationItem(
                    title = "Open Source Licenses",
                    onClick = { /* Navigate to licenses */ }
                )
            }
        }
    }
}

@Composable
fun SettingsCategory(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsSliderItem(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f
        )
    }
}

@Composable
fun SettingsNavigationItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Navigate"
        )
    }
}

@Composable
fun SettingsInfoItem(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}