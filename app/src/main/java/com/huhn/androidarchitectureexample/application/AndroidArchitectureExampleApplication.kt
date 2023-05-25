package com.huhn.androidarchitectureexample.application

import android.app.Application
import com.huhn.androidarchitectureexample.dependencyInjection.koinModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

@HiltAndroidApp
class AndroidArchitectureExampleApplication :Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AndroidArchitectureExampleApplication)
            modules(listOf(koinModule))
        }
    }
}