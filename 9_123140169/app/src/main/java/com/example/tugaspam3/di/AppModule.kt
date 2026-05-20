package com.example.tugaspam3.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.tugaspam3.BuildConfig
import com.example.tugaspam3.data.ApiService
import com.example.tugaspam3.data.GeminiService
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.data.SettingsManager
import com.example.tugaspam3.db.NoteDatabase
import com.example.tugaspam3.platform.*
import com.example.tugaspam3.viewmodel.AiViewModel
import com.example.tugaspam3.viewmodel.NotesViewModel
import com.example.tugaspam3.viewmodel.ProfileViewModel
import com.example.tugaspam3.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Main Koin module for dependency injection.
 * Handles database, services, repositories, and viewmodels.
 */
val appModule = module {
    // Database injection
    single {
        val driver = AndroidSqliteDriver(NoteDatabase.Schema, androidContext(), "notes.db")
        NoteDatabase(driver)
    }

    // Preferences/DataStore injection
    single { SettingsManager(androidContext()) }

    // Platform features (Bonus included)
    single<DeviceInfo> { AndroidDeviceInfo() }
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    single<BatteryInfo> { AndroidBatteryInfo(androidContext()) }

    // Remote API Service
    single {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Gemini AI Service
    // Note: Make sure to add gemini_api_key in local.properties or as an environment variable
    single { GeminiService(apiKey = "AIzaSyBt_0XlcKpODI3Zzi_g6gdFmG8n_7CGxLQ") }

    // Repository for data operations
    single { NoteRepository(get(), get()) }

    // ViewModels injection
    viewModel { NotesViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel() }
    viewModel { AiViewModel(get()) }
}
