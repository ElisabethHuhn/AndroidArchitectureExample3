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
    data object ToggleIsSorted : DriverUserEvent
    data object ResetIsSorted : DriverUserEvent
    data object GetDrivers : DriverUserEvent
    data class SaveDriver(val driver : Driver) : DriverUserEvent
    data object PrintDrivers : DriverUserEvent
    data object DeleteDriversRoutes : DriverUserEvent
}
