package com.huhn.androidarchitectureexample.viewmodel

import com.huhn.androidarchitectureexample.repository.DriverRepository
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineScope


class TestDriverRepositoryImpl() : DriverRepository {
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
    fun getIsEmpty() = isEmpty
    //TODO Throw error exception somehow

    override suspend fun fetchDriversAndRoutes(
        isSorted: Boolean,
        scope: CoroutineScope
    ): DriverResponse {
        isEmpty = false
        return defaultDriverResponse()
    }

    override suspend fun deleteAllDriversAndRoutesLocal()  {
        isEmpty = true
    }
}