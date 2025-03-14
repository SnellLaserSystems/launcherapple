package com.example.launcherapple
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.launcherapple.data.AppInfo
import com.example.launcherapple.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    // Apps for the home screen
    private val _homeScreenApps = mutableStateListOf<AppInfo>()
    val homeScreenApps: List<AppInfo> = _homeScreenApps

    // Apps for the dock
    private val _dockApps = mutableStateListOf<AppInfo>()
    val dockApps: List<AppInfo> = _dockApps

    // All apps for the library view
    private val _allApps = mutableStateListOf<AppInfo>()
    val allApps: List<AppInfo> = _allApps

    // App library visibility
    private val _isAppLibraryVisible = mutableStateOf(false)
    val isAppLibraryVisible = _isAppLibraryVisible

    // Current page in pager
    private val _currentPage = mutableStateOf(0)
    val currentPage = _currentPage

    // Search query
    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    init {
        // Default apps will be loaded when viewModel is created
    }

    fun loadApps(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val apps = appRepository.getAllApps()

                _allApps.clear()
                _allApps.addAll(apps)

                // Separate dock apps
                val dock = apps.filter { it.isDockApp }.take(4)
                _dockApps.clear()
                _dockApps.addAll(dock)

                // Other apps for home screen
                val homeApps = apps.filter { !it.isDockApp }.take(20)
                _homeScreenApps.clear()
                _homeScreenApps.addAll(homeApps)
            }
        }
    }

    fun launchApp(context: Context, packageName: String) {
        appRepository.launchApp(context, packageName)
    }

    fun updateDockApps(newList: List<AppInfo>) {
        _dockApps.clear()
        _dockApps.addAll(newList)
    }

    fun updateHomeScreenApps(newList: List<AppInfo>) {
        _homeScreenApps.clear()
        _homeScreenApps.addAll(newList)
    }

    fun moveAppInHomeScreen(fromIndex: Int, toIndex: Int) {
        if (fromIndex < _homeScreenApps.size && toIndex < _homeScreenApps.size) {
            val app = _homeScreenApps[fromIndex]
            _homeScreenApps.removeAt(fromIndex)
            _homeScreenApps.add(toIndex, app)
        }
    }

    fun moveAppInDock(fromIndex: Int, toIndex: Int) {
        if (fromIndex < _dockApps.size && toIndex < _dockApps.size) {
            val app = _dockApps[fromIndex]
            _dockApps.removeAt(fromIndex)
            _dockApps.add(toIndex, app)
        }
    }

    fun showAppLibrary() {
        _isAppLibraryVisible.value = true
    }

    fun hideAppLibrary() {
        _isAppLibraryVisible.value = false
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredApps(): List<AppInfo> {
        val query = searchQuery.value.lowercase()
        return if (query.isEmpty()) {
            allApps
        } else {
            allApps.filter { it.label.lowercase().contains(query) }
        }
    }
}
