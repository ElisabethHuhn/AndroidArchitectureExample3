package com.huhn.androidarchitectureexample.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huhn.androidarchitectureexample.repository.database.dbModel.DBDriver
import com.huhn.androidarchitectureexample.repository.database.dbModel.DBRoute

@Database(entities = [DBDriver::class, DBRoute::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbDriverDao(): DBDriverDao
    abstract fun dbRouteDao(): DBRouteDao
}