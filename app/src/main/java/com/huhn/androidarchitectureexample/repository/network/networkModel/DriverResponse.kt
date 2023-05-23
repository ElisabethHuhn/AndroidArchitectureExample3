package com.huhn.androidarchitectureexample.repository.network.networkModel

data class DriverResponse(
    val drivers: List<Driver>,
    val routes: List<Route>
)