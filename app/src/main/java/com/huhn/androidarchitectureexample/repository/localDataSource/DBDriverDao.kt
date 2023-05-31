package com.huhn.androidarchitectureexample.repository.localDataSource

import androidx.room.Dao
import androidx.room.Query
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBDriver

@Dao
interface DBDriverDao {
    @Query("SELECT * FROM DBDriver")
    suspend fun getAllDrivers(): List<DBDriver>

//    @Query("SELECT * FROM DBDriver WHERE uid IN (:DBDriverIds)")
//    suspend fun loadAllDriversByIds(DBDriverIds: IntArray): List<DBDriver>
//
//    @Query("SELECT * FROM DBDriver WHERE name LIKE :name  LIMIT 1")
//    suspend fun findDriverByName(name: String): DBDriver
//
//    @Insert
//    suspend fun insertAllDrivers(vararg DBDrivers: DBDriver)
//
//    @Update
//    suspend fun updateDriver(driver: DBDriver)
//
//    @Delete
//    suspend fun deleteDriver(DBDriver: DBDriver)
}