package com.huhn.androidarchitectureexample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huhn.androidarchitectureexample.repository.DriverRepositoryImpl
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.flow.MutableStateFlow

// TODO: Figure out how to do this using Koin
//interface DriverViewModel {
//    var drivers: MutableLiveData<List<Driver>>
//    var routes: MutableLiveData<List<Route>>
//
//    fun getDrivers()
//}

class DriverViewModelImpl(private val repo : DriverRepositoryImpl) : ViewModel()
{
    private var _isSorted = MutableLiveData<Boolean>()
    val isSorted : LiveData<Boolean>
        get() = _isSorted
    fun toggleIsSorted() {
        var newIsSorted = true
        if (isSorted.value == true) newIsSorted = false
        setIsSorted(newIsSorted)
    }
    private fun setIsSorted(isSorted: Boolean) {
        _isSorted.value = isSorted
    }

    val driversFlow : MutableStateFlow<List<Driver>> = MutableStateFlow(listOf())
    val foundDriverFlow : MutableStateFlow<Driver> = MutableStateFlow(Driver(id = "0", name = "Zero"))

    val routesFlow : MutableStateFlow<List<Route>> = MutableStateFlow(listOf())

    fun getDrivers()  {
        val isSortedFlag = isSorted.value ?: false
        repo.getDrivers(
            driversFlow = driversFlow,
            routesFlow = routesFlow,
            isSorted = isSortedFlag
        )
    }


    fun findDriver(driverId: String) {
        // TODO: return a specific route based on business rules
        //for now just punt
        repo.findDriver(driverId = driverId, foundFlow = foundDriverFlow)
        //The UI uses the flow as state, and thus gets updated on the return
    }


    fun findRoute(driverId: String): Route {
        //TODO actually implement this using business rules from problem domain
        return Route(id= 0, name = "Route Zero", type = "I")
    }

    fun deleteDriver(driver: Driver) {
        repo.deleteDriverLocal(driver = driver)
    }

    fun deleteRoute(route: Route) {
        repo.deleteRouteLocal(route = route)
    }

    fun printDrivers(driverList: List<Driver>) {
        println("List of Drivers:")
        driverList.forEach { driver ->
            val msg = "DriverID: ${driver.id} is named: ${driver.name} "
            println( msg)
        }
    }
}