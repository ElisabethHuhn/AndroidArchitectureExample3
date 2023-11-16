package com.huhn.androidarchitectureexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huhn.androidarchitectureexample.repository.DriverRepositoryImpl
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DriverViewModel(
    private val repository: DriverRepositoryImpl
) : ViewModel()
{
    private val _driverState = MutableStateFlow(DriverState())
    var driverState = _driverState.asStateFlow()

    fun onDriverUserEvent(event: DriverUserEvent) {
        when (event) {
            is DriverUserEvent.ToggleIsSorted -> toggleIsSorted()
            is DriverUserEvent.ResetIsSorted -> resetIsSorted()
            is DriverUserEvent.GetDrivers -> getDrivers()
            is DriverUserEvent.SaveDriver -> onSaveDriverChanged(event.driver)
            is DriverUserEvent.PrintDrivers -> printDrivers()
            is DriverUserEvent.DeleteDriversRoutes -> deleteDriversRoutes()
            is DriverUserEvent.ClearError -> onClearError()
        }
    }

    //region state update functions
    private fun toggleIsSorted() {
        var newIsSorted = true
        if (_driverState.value.isSorted) newIsSorted = false

        _driverState.update {
            it.copy(isSorted = newIsSorted)
        }
    }

    private fun resetIsSorted() {
        _driverState.update {
            it.copy(isSorted = false)
        }
    }

    private fun onDriverListChanged (drivers: List<Driver>) {
        _driverState.update {
            it.copy(drivers = drivers)
        }
    }

    private fun onRouteListChanged (routes: List<Route>) {
        _driverState.update {
            it.copy(routes = routes)
        }
    }

    private fun onSaveDriverChanged(driver: Driver?){
        _driverState.update {
            it.copy(savedDriver = driver)
        }
    }

    private fun onClearError(){
        _driverState.update {
            it.copy(errors = "")
        }
    }

    //end region

    //region actions in response to user event triggers. See onUserEvent() above

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _driverState.update {
            it.copy(errors = throwable.message ?: "CoroutineExceptionHandler : Error loading drivers and routes")
        }
    }

    private fun getDrivers()  {
        val isSortedFlag = _driverState.value.isSorted
        viewModelScope.launch(exceptionHandler) {
            try {
                val driverResponse = repository.fetchDriversAndRoutes(
                    isSorted = isSortedFlag,
                    viewModelScope
                )
                //now update the state with the lists
                onDriverListChanged(drivers = driverResponse.drivers)
                onRouteListChanged(routes = driverResponse.routes)
            }
            catch (e: Exception) {
                _driverState.update {
                    it.copy(errors = e.message ?: "Try/Catch: Error loading drivers and routes")
                }
            }
        }
    }

    private fun deleteDriversRoutes () {
        deleteDrivers()
        deleteRoutes()
        onDriverListChanged(listOf())
        onDriverUserEvent(DriverUserEvent.GetDrivers)
    }
    private fun deleteDrivers () {
        _driverState.value.drivers?.forEach { driver ->
            deleteDriver(driver)
        }
    }
    private fun deleteDriver(driver: Driver) {
        viewModelScope.launch {
            repository.deleteDriverLocal(driver = driver)
        }
    }

    private fun deleteRoutes(){
        _driverState.value.routes?.forEach { route ->
            deleteRoute(route)
        }
    }

    private fun deleteRoute(route: Route) {
        viewModelScope.launch {
            repository.deleteRouteLocal(route = route)
        }
    }

    private fun printDrivers() {
        val driverList: List<Driver> = _driverState.value.drivers ?: listOf()
        println("List of Drivers:")
        driverList.forEach { driver ->
            val msg = "DriverID: ${driver.id} is named: ${driver.name} "
            println( msg)
        }
    }

    //end region
}