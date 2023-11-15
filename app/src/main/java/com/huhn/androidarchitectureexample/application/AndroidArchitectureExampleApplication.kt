package com.huhn.androidarchitectureexample.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.huhn.androidarchitectureexample.dependencyInjection.koinModule
import com.huhn.androidarchitectureexample.repository.localDataSource.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class AndroidArchitectureExampleApplication :Application() {
    companion object {
        lateinit  var appContext: Context
        lateinit var roomDb: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this.applicationContext

        startKoin {
            androidLogger()
            androidContext(this@AndroidArchitectureExampleApplication)
            modules(listOf(koinModule))
        }

        //create Room DB
        roomDb = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "aae-database"
        ).build()
    }
}