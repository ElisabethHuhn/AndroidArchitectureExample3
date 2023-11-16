package com.huhn.androidarchitectureexample.repository.localDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBDriver

@Dao
interface DBDriverDao {
//    @Query("SELECT * FROM DBDriver")
//    suspend fun getAllDrivers(): List<DBDriver>

    //    @Query("SELECT * FROM DBDriver WHERE uid IN (:DBDriverIds)")
//    suspend fun loadAllDriversByIds(DBDriverIds: IntArray): List<DBDriver>
//
    @Query("SELECT * FROM DBDriver WHERE uid LIKE :driverId LIMIT 1")
    suspend fun findDriversById(driverId : Int): DBDriver

    @Query("SELECT * FROM DBDriver WHERE name LIKE :name  LIMIT 1")
    suspend fun findDriverByName(name: String): DBDriver

//    @Query("SELECT * FROM DBDriver ORDER BY name")
//    suspend fun sortDriverByName(): List<DBDriver>

    @Query("SELECT * FROM DBDriver ORDER BY uid ASC")
    suspend fun sortDriverByAscId(): List<DBDriver>

    @Query("SELECT * FROM DBDriver ORDER BY uid DESC")
    suspend fun sortDriverByDescId(): List<DBDriver>

    @Insert
    suspend fun insertAllDrivers(vararg dbDrivers: DBDriver)

//    @Update
//    suspend fun updateDriver(driver: DBDriver)

    @Delete
    suspend fun deleteDriver(dbDriver: DBDriver)

    @Query("DELETE FROM DBDriver")
    suspend fun deleteAll()
}