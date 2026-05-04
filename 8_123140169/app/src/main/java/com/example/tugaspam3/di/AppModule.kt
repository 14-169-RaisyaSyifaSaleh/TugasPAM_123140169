package com.example.tugaspam3.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.tugaspam3.data.ApiService
import com.example.tugaspam3.data.NoteRepository
import com.example.tugaspam3.data.SettingsManager
import com.example.tugaspam3.db.NoteDatabase
import com.example.tugaspam3.platform.*
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

    // Repository for data operations
    single { NoteRepository(get(), get()) }

    // ViewModels injection
    viewModel { NotesViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel() }
}
