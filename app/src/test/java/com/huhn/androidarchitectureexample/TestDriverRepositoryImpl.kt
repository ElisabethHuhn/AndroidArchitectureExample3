package com.huhn.androidarchitectureexample

import com.huhn.androidarchitectureexample.repository.DriverRepository
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineScope


class TestDriverRepositoryImpl : DriverRepository {
    companion object{
        fun defaultDriverResponse(): DriverResponse {
            return DriverResponse(
                listOf(
                    defaultDriver(),
                    Driver(id = "2", name = "Jane Doe"),
                    Driver(id = "3", name = "John Smith"),
                    Driver(id = "4", name = "Jane Smith"),
                    Driver(id = "5", name = "John Jones"),
                    Driver(id = "6", name = "Jane Jones"),
                ),
                listOf(
                    defaultRoute(),
                    Route(id = 2, name = "North Side", type = "C"),
                    Route(id = 3, name = "East Side", type = "I"),
                    Route(id = 4, name = "West Side", type = "R"),
                    Route(id = 5, name = "Downtown", type = "C"),
                    Route(id = 6, name = "Uptown", type = "R"),
                    Route(id = 7, name = "South Side Industrial", type = "I"),
                )
            )
        }

        fun emptyDriverResponse(): DriverResponse {
            return DriverResponse(
                listOf( ),
                listOf( )
            )
        }

        fun defaultDriver() = Driver(id = "1", name = "John Doe")
        fun defaultRoute() = Route(id = 1, name = "South Side", type = "R")
    }

    private var isEmpty = true

    override suspend fun fetchDriversAndRoutes(
        isSorted: Boolean,
        scope: CoroutineScope
    ): DriverResponse {
        isEmpty = false
        return defaultDriverResponse()
    }

    override suspend fun getDriversAndRoutesLocal(
        isSorted: Boolean,
        dbScope: CoroutineScope
    ): DriverResponse {
        return if(isEmpty) emptyDriverResponse()
        else defaultDriverResponse()

    }

    override suspend fun deleteAllDriversAndRoutesLocal()  {
        isEmpty = true
    }

    override suspend fun getDriversLocal(isSorted: Boolean) : List<Driver> {
        return if (isEmpty) listOf()
        else defaultDriverResponse().drivers
    }

    override suspend fun getRoutesLocal() : List<Route> {
        return if (isEmpty) listOf()
        else defaultDriverResponse().routes
    }

    override suspend fun getDriversAndRoutesRemote(
        isSorted: Boolean,
        dbScope: CoroutineScope
    ) : DriverResponse {
        return defaultDriverResponse()
    }

    override suspend fun insertDriversAndRoutesIntoDb(
        driverResponse: DriverResponse,
        dbScope: CoroutineScope
    ) {
        insertDriver(driverResponse)
        insertRoute(driverResponse)
    }

    override suspend fun insertDriver(driverResponse: DriverResponse) {
        isEmpty = driverResponse.drivers.isEmpty()
    }
    override suspend fun insertRoute(driverResponse: DriverResponse) {
        isEmpty = driverResponse.routes.isEmpty()
    }
}