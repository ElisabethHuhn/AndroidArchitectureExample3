package com.huhn.androidarchitectureexample.repository.localDataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBDriver
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBRoute

@Database(entities = [DBDriver::class, DBRoute::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbDriverDao(): DBDriverDao
    abstract fun dbRouteDao(): DBRouteDao
}