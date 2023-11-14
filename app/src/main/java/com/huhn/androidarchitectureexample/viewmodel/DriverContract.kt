package com.huhn.androidarchitectureexample.viewmodel

import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route

data class DriverState(
    val drivers : List<Driver>? = null,
    val selectedDriver : Driver? = null,
    val savedDriver: Driver? = null,
    val isSorted: Boolean = false,
    val routes : List<Route>? = null
    )

sealed interface DriverUserEvent {
    object ToggleIsSorted : DriverUserEvent
    object ResetIsSorted : DriverUserEvent
    object GetDrivers : DriverUserEvent
    data class SaveDriver(val driver : Driver) : DriverUserEvent
    object PrintDrivers : DriverUserEvent
    object DeleteDriversRoutes : DriverUserEvent
}
