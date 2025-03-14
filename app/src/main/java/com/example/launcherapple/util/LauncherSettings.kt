package com.example.launcherapple.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.launcherapple.data.AppInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LauncherSettings @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        LAUNCHER_PREFERENCES, Context.MODE_PRIVATE
    )

    // Save/restore dock app positions
    fun saveDockApps(apps: List<AppInfo>) {
        val packageNames = apps.joinToString(",") { it.packageName }
        prefs.edit {
            putString(KEY_DOCK_APPS, packageNames)
        }
    }

    fun getDockAppPackages(): List<String> {
        val packageNames = prefs.getString(KEY_DOCK_APPS, null)
        return packageNames?.split(",") ?: emptyList()
    }

    // Save/restore home screen layout
    fun saveHomeScreenLayout(pageApps: Map<Int, List<String>>) {
        pageApps.forEach { (page, apps) ->
            val appsString = apps.joinToString(",")
            prefs.edit {
                putString(KEY_HOME_SCREEN + "_" + page, appsString)
            }
        }
    }

    fun getHomeScreenLayout(page: Int): List<String> {
        val appsString = prefs.getString(KEY_HOME_SCREEN + "_" + page, null)
        return appsString?.split(",") ?: emptyList()
    }

    // Wallpaper settings
    fun saveWallpaperDimming(dimming: Float) {
        prefs.edit {
            putFloat(KEY_WALLPAPER_DIMMING, dimming)
        }
    }

    fun getWallpaperDimming(): Float {
        return prefs.getFloat(KEY_WALLPAPER_DIMMING, 0f)
    }

    companion object {
        private const val LAUNCHER_PREFERENCES = "launcher_prefs"
        private const val KEY_DOCK_APPS = "dock_apps"
        private const val KEY_HOME_SCREEN = "home_screen"
        private const val KEY_WALLPAPER_DIMMING = "wallpaper_dimming"
    }
}