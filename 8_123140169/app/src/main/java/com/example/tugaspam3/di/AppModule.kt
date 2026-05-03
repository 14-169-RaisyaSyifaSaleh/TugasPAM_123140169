package com.example.tugaspam3.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.data.SettingsManager
import com.example.tugaspam3.db.NoteDatabase
import com.example.tugaspam3.platform.AndroidDeviceInfo
import com.example.tugaspam3.platform.AndroidNetworkMonitor
import com.example.tugaspam3.platform.DeviceInfo
import com.example.tugaspam3.platform.NetworkMonitor
import com.example.tugaspam3.viewmodel.NotesViewModel
import com.example.tugaspam3.viewmodel.ProfileViewModel
import com.example.tugaspam3.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database & SQLDelight
    single<SqlDriver> {
        AndroidSqliteDriver(NoteDatabase.Schema, androidContext(), "notes.db")
    }
    single { NoteDatabase(get()) }
    
    // Data Sources
    single { NoteRepository(get()) }
    single { SettingsManager(androidContext()) }
    
    // Platform Features
    single<DeviceInfo> { AndroidDeviceInfo(androidContext()) }
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    
    // ViewModels
    viewModel { NotesViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ProfileViewModel() }
}
