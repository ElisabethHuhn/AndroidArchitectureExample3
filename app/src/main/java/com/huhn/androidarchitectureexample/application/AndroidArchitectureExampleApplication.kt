package com.huhn.androidarchitectureexample.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidArchitectureExampleApplication :Application() {
    override fun onCreate() {
        super.onCreate()

//        startKoin {
//
//        }
    }
}