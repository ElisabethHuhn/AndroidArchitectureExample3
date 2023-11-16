package com.huhn.androidarchitectureexample.repository

import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineScope

interface DriverRepository {
    suspend fun fetchDriversAndRoutes(isSorted: Boolean, scope: CoroutineScope) : DriverResponse
    suspend fun deleteDriverLocal(driver: Driver)
    suspend fun deleteRouteLocal(route: Route)
}