package com.huhn.androidarchitectureexample.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.huhn.androidarchitectureexample.repository.database.dbModel.DBDriver

@Dao
interface DBDriverDao {
    @Query("SELECT * FROM DBDriver")
    suspend fun getAll(): List<DBDriver>

//    @Query("SELECT * FROM DBDriver WHERE uid IN (:DBDriverIds)")
//    suspend fun loadAllByIds(DBDriverIds: IntArray): List<DBDriver>
//
//    @Query("SELECT * FROM DBDriver WHERE name LIKE :name  LIMIT 1")
//    suspend fun findByName(name: String): DBDriver
//
//    @Insert
//    suspend fun insertAll(vararg DBDrivers: DBDriver)
//
//    @Delete
//    suspend fun delete(DBDriver: DBDriver)
}