// MainActivity.kt
package com.example.launcherapple

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.launcherapple.data.AppInfo
import com.example.launcherapple.ui.HomeScreen
import com.example.launcherapple.ui.WidgetsScreen
import com.example.launcherapple.ui.animation.AppOpenAnimation
import com.example.launcherapple.ui.settings.SettingsScreen
import com.example.launcherapple.ui.theme.IOSLauncherTheme
import com.example.launcherapple.di.AppModule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Use viewModels() delegate to get the ViewModel in a way that's scoped to this Activity's lifecycle
    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")

        setContent {
            IOSLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // State for tracking app opening animations
                    var appOpeningAnimation by remember { mutableStateOf(false) }
                    var selectedApp by remember { mutableStateOf<AppInfo?>(null) }

                    // Loading indicator
                    if (viewModel.isLoading.value) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") {
                                Log.d("MainActivity", "Home screen composable")
                                Log.d("MainActivity", "Home apps: ${viewModel.homeScreenApps.size}")
                                Log.d("MainActivity", "Dock apps: ${viewModel.dockApps.size}")

                                HomeScreen(
                                    viewModel = viewModel,
                                    onSettingsClick = { navController.navigate("settings") },
                                    onWidgetsClick = { navController.navigate("widgets") },
                                    onAppClick = { app ->
                                        Log.d("MainActivity", "App click detected: ${app.label}")
                                        // Direct launch without animation for testing
                                        viewModel.launchApp(this@MainActivity, app.packageName)

                                        // If you want to enable animation later:
                                        /*
                                        selectedApp = app
                                        appOpeningAnimation = true
                                        */
                                    }
                                )

                                // Handle app opening animation if needed
                                selectedApp?.let { app ->
                                    if (appOpeningAnimation) {
                                        AppOpenAnimation(
                                            app = app,
                                            isVisible = appOpeningAnimation,
                                            onAnimationComplete = {
                                                appOpeningAnimation = false
                                                viewModel.launchApp(this@MainActivity, app.packageName)
                                            }
                                        )
                                    }
                                }
                            }

                            composable("widgets") {
                                WidgetsScreen()
                            }

                            composable("settings") {
                                SettingsScreen(
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
        // Refresh app list when returning to launcher
        viewModel.loadApps(this)
    }
}