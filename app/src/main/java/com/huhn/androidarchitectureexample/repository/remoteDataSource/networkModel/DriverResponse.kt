package com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel

data class DriverResponse(
    val drivers: List<Driver>,
    val routes: List<Route>
)