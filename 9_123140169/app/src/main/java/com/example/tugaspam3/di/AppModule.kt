package com.example.tugaspam3.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.tugaspam3.data.GeminiService
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
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
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
    
    // AI Services
    single {
        val apiKey = "AIzaSyB2vdIx2z3Wgp3Oy52arpY93coCi2PkKbI"
        GenerativeModel(
            // Menggunakan model gemini-2.5-flash sesuai screenshot terbaru
            modelName = "gemini-2.5-flash",
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
                topK = 40
                topP = 0.95f
                maxOutputTokens = 1024
            }
        )
    }
    single { GeminiService(get()) }
    
    // Platform Features
    single<DeviceInfo> { AndroidDeviceInfo(androidContext()) }
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    
    // ViewModels
    viewModel { NotesViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ProfileViewModel() }
}
