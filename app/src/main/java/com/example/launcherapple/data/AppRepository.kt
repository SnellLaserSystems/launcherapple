// data/AppRepository.kt
package com.example.launcherapple.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(@ApplicationContext private val context: Context) {

    fun getAllApps(): List<AppInfo> {
        Log.d("AppRepository", "Loading all apps")

        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val appsList = mutableListOf<AppInfo>()

        try {
            // Query for all apps that can be launched
            val activities = packageManager.queryIntentActivities(intent, 0)
            Log.d("AppRepository", "Found ${activities.size} activities")

            // Sort activities by app name for consistent order
            val sortedActivities = activities.sortedBy {
                it.loadLabel(packageManager).toString()
            }

            // Convert ResolveInfo to AppInfo
            sortedActivities.forEachIndexed { index, resolveInfo ->
                val packageName = resolveInfo.activityInfo.packageName

                // Skip the launcher itself to avoid recursion
                if (!packageName.equals("com.example.launcherapple")) {
                    val appLabel = resolveInfo.loadLabel(packageManager).toString()
                    val appIcon = resolveInfo.loadIcon(packageManager)

                    // Default dock apps (first 4 apps in alphabetical order)
                    val isDockApp = index < 4

                    // Group apps into categories (simplified for this example)
                    val category = when {
                        packageName.contains("camera") -> "Photography"
                        packageName.contains("music") || packageName.contains("audio") -> "Entertainment"
                        packageName.contains("mail") || packageName.contains("messaging") -> "Communication"
                        packageName.contains("game") -> "Games"
                        packageName.contains("map") || packageName.contains("navigation") -> "Navigation"
                        else -> "Other"
                    }

                    appsList.add(
                        AppInfo(
                            packageName = packageName,
                            label = appLabel,
                            icon = appIcon,
                            category = category,
                            position = index,
                            isDockApp = isDockApp
                        )
                    )

                    Log.d("AppRepository", "Added app: $appLabel ($packageName)")
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error loading apps", e)
        }

        Log.d("AppRepository", "Returned ${appsList.size} apps")
        return appsList
    }

    fun launchApp(context: Context, packageName: String) {
        try {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                Log.d("AppRepository", "Launched app: $packageName")
            } else {
                Log.e("AppRepository", "Could not get launch intent for: $packageName")
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error launching app: $packageName", e)
        }
    }
}