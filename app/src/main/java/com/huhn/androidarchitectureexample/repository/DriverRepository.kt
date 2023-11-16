package com.huhn.androidarchitectureexample.repository

import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import kotlinx.coroutines.CoroutineScope

interface DriverRepository {
    suspend fun fetchDriversAndRoutes(isSorted: Boolean, scope: CoroutineScope) : DriverResponse
    suspend fun deleteAllDriversAndRoutesLocal()
}