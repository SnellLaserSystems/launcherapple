package com.example.launcherapple.ui

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.launcherapple.LauncherViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.OutlinedTextField

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: LauncherViewModel) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 2 })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Search bar

            Spacer(modifier = Modifier.height(24.dp))

            // Grid display of apps


            // Main home screen areas with pages
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> AppGrid(
                        apps = viewModel.homeScreenApps,
                        onAppClick = { app ->
                            viewModel.launchApp(context, app.packageName)
                        },
                        onAppLongClick = { /* Handle app editing mode */ },
                        onAppMove = { fromIndex, toIndex ->
                            viewModel.moveAppInHomeScreen(fromIndex, toIndex)
                        }
                    )
                    1 -> Box(modifier = Modifier.fillMaxSize()) {
                        // Second page content if needed
                    }
                }
            }

            // Spacer before dock
            Spacer(modifier = Modifier.height(4.dp))

            // Dock area
            LauncherDock(
                apps = viewModel.dockApps,
                onAppClick = { app ->
                    viewModel.launchApp(context, app.packageName)
                },
                onAppLongClick = { /* Handle dock app editing */ },
                onAppMove = { fromIndex, toIndex ->
                    viewModel.moveAppInDock(fromIndex, toIndex)
                }
            )
        }

            // App Library / Search overlay
            AnimatedVisibility(
                visible = viewModel.isAppLibraryVisible.value,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                AppLibrary(
                    apps = viewModel.getFilteredApps(),
                    searchQuery = viewModel.searchQuery.value,
                    onSearchQueryChange = { query ->
                        viewModel.updateSearchQuery(query)
                    },
                    onAppClick = { app ->
                        viewModel.launchApp(context, app.packageName)
                        viewModel.hideAppLibrary()
                    },
                    onDismiss = {
                        viewModel.hideAppLibrary()
                    }
                )
            }

            // Swipe up gesture to show app library
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        var startY = 0f
                        detectDragGestures(
                            onDragStart = { offset ->
                                startY = offset.y
                            },
                            onDragEnd = {
                                viewModel.showAppLibrary()
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                // Only show app library on significant upward swipe
                                if (startY - change.position.y > 100) {
                                    viewModel.showAppLibrary()
                                }
                            }
                        )
                    }
            )

            // Page indicator

        }
    }
