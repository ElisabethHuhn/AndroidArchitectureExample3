package com.huhn.androidarchitectureexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huhn.androidarchitectureexample.repository.DriverRepository
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class DriverViewModel(
    private val repository: DriverRepository
) : ViewModel()
{
    protected val _driverState = MutableStateFlow(DriverState())
    var driverState = _driverState.asStateFlow()

    fun onDriverUserEvent(event: DriverUserEvent) {
        when (event) {
            is DriverUserEvent.ToggleIsSorted -> toggleIsSorted()
            is DriverUserEvent.ResetIsSorted -> resetIsSorted()
            is DriverUserEvent.GetDrivers -> getDrivers()
            is DriverUserEvent.SelectDriver -> onSelectedDriverChanged(event.driver)
            is DriverUserEvent.PrintDrivers -> printDrivers()
            is DriverUserEvent.DeleteDriversRoutes -> deleteAllDriversRoutesLocal()
            is DriverUserEvent.SetError -> onErrorChanged(event.error)
            is DriverUserEvent.ClearError -> onClearError()
        }
    }

    //region state update functions
    protected open fun toggleIsSorted() {
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

    protected fun onDriverListChanged (drivers: List<Driver>) {
        _driverState.update {
            it.copy(drivers = drivers)
        }
    }

    protected fun onRouteListChanged (routes: List<Route>) {
        _driverState.update {
            it.copy(routes = routes)
        }
    }

    private fun onSelectedDriverChanged(driver: Driver?){
        _driverState.update {
            it.copy(selectedDriver = driver)
        }
    }


    protected fun onErrorChanged(error: String){
        _driverState.update {
            it.copy(errors = error)
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
                onErrorChanged(e.message ?: "Try/Catch: Error loading drivers and routes")
            }
        }
    }

    private fun deleteAllDriversRoutesLocal () {
        viewModelScope.launch {
            repository.deleteAllDriversAndRoutesLocal()
        }

        onDriverListChanged(listOf())
        onRouteListChanged(listOf())
    }

    protected open fun printDrivers() {
        val driverList: List<Driver> = _driverState.value.drivers ?: listOf()
        println("List of Drivers:")
        driverList.forEach { driver ->
            val msg = "DriverID: ${driver.id} is named: ${driver.name} "
            println( msg)
        }
    }

    //end region
}