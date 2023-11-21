package com.huhn.androidarchitectureexample.repository

import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineScope

interface DriverRepository {
    suspend fun fetchDriversAndRoutes(isSorted: Boolean, scope: CoroutineScope) : DriverResponse
    suspend fun deleteAllDriversAndRoutesLocal()

    suspend fun getDriversAndRoutesLocal(
        isSorted: Boolean,
        dbScope: CoroutineScope
    ): DriverResponse
    suspend fun getDriversLocal(isSorted: Boolean) : List<Driver>
    suspend fun getRoutesLocal() : List<Route>

    suspend fun getDriversAndRoutesRemote(
        isSorted: Boolean,
        dbScope: CoroutineScope
    ) : DriverResponse

    suspend fun insertDriversAndRoutesIntoDb(
        driverResponse: DriverResponse,
        dbScope: CoroutineScope
    )
    suspend fun insertDriver(driverResponse: DriverResponse)
    suspend fun insertRoute(driverResponse: DriverResponse)
}