package com.example.tugaspam3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugaspam3.data.SettingsManager
import com.example.tugaspam3.data.SortOrder
import com.example.tugaspam3.platform.DeviceInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager,
    val deviceInfo: DeviceInfo
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = settingsManager.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val sortOrder: StateFlow<SortOrder> = settingsManager.sortOrder
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SortOrder.BY_DATE)

    fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            settingsManager.updateDarkMode(isDarkMode)
        }
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            settingsManager.updateSortOrder(sortOrder)
        }
    }
}
