package com.huhn.androidarchitectureexample.viewmodel

import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route

data class DriverState(
    val drivers : List<Driver>? = listOf(),
    val selectedDriver: Driver? = null,
    val isSorted: Boolean = false,
    val routes : List<Route>? = null,
    val errors: String = ""
    )

sealed interface DriverUserEvent {
    data object ToggleIsSorted : DriverUserEvent
    data object ResetIsSorted : DriverUserEvent
    data object GetDrivers : DriverUserEvent
    data class SelectDriver(val driver : Driver) : DriverUserEvent
    data object PrintDrivers : DriverUserEvent
    data object DeleteDriversRoutes : DriverUserEvent
    data class SetError(val error: String) : DriverUserEvent
    data object ClearError : DriverUserEvent
}
