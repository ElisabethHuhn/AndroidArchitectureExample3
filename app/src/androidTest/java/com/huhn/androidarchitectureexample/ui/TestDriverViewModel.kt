package com.huhn.androidarchitectureexample.ui

import com.huhn.androidarchitectureexample.TestDriverDataImpl
import com.huhn.androidarchitectureexample.repository.DriverRepository
import com.huhn.androidarchitectureexample.viewmodel.DriverViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.update

class TestDriverViewModel(
    private val repository: DriverRepository
) : DriverViewModel(repository)
{
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _driverState.update {
            it.copy(errors = throwable.message ?: "CoroutineExceptionHandler : Error loading drivers and routes")
        }
    }

    private fun getDrivers()  {
        val isSortedFlag = _driverState.value.isSorted
        //now update the state with the lists
        onDriverListChanged(drivers = TestDriverDataImpl.defaultDriverResponse().drivers)
        onRouteListChanged(routes = TestDriverDataImpl.defaultDriverResponse().routes)
    }

    private fun deleteAllDriversRoutesLocal () {
        onDriverListChanged(listOf())
        onRouteListChanged(listOf())
    }
}