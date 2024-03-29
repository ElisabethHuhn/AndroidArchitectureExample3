package com.huhn.androidarchitectureexample.repository.localDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBRoute

@Dao
interface DBRouteDao {
    @Query("SELECT * FROM DBRoute")
    suspend fun getAllRoutes(): List<DBRoute>

//    @Query("SELECT * FROM DBRoute WHERE uid IN (:DBRouteIds)")
//    suspend fun loadAllRoutesByIds(DBRouteIds: IntArray): List<DBRoute>

    @Query("SELECT * FROM DBRoute WHERE name LIKE :name  LIMIT 1")
    suspend fun findRouteByName(name: String): DBRoute

    @Insert
    suspend fun insertAllRoutes(vararg dbRoutes: DBRoute)

//    @Update
//    suspend fun updateRoute(route: DBRoute)

    @Delete
    suspend fun deleteRoute(dbRoute: DBRoute)

    @Query("DELETE FROM DBRoute")
    suspend fun deleteAll()
}