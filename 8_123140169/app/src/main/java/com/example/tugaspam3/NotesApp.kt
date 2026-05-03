package com.example.tugaspam3

import android.app.Application
import com.example.tugaspam3.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NotesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@NotesApp)
            modules(appModule)
        }
    }
}
