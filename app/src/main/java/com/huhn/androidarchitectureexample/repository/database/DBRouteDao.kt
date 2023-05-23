package com.huhn.androidarchitectureexample.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.huhn.androidarchitectureexample.repository.database.dbModel.DBRoute

@Dao
interface DBRouteDao {
    @Query("SELECT * FROM DBRoute")
    suspend fun getAll(): List<DBRoute>

//    @Query("SELECT * FROM DBRoute WHERE uid IN (:DBRouteIds)")
//    suspend fun loadAllByIds(DBRouteIds: IntArray): List<DBRoute>
//
//    @Query("SELECT * FROM DBRoute WHERE name LIKE :name  LIMIT 1")
//    suspend fun findByName(name: String): DBRoute
//
//    @Insert
//    suspend fun insertAll(vararg DBRoutes: DBRoute)
//
//    @Delete
//    suspend fun delete(DBRoute: DBRoute)
}