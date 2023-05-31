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
    var isSorted = false

    var _drivers = MutableLiveData<List<Driver>>()
    val drivers : LiveData<List<Driver>>
        get() = _drivers
    val driversFlow : MutableStateFlow<List<Driver>> = MutableStateFlow(listOf())

//    var _routes = MutableLiveData<List<Route>>()
//    val routes : MutableLiveData<List<Route>>
//        get() = _routes
    val routesFlow : MutableStateFlow<List<Route>> = MutableStateFlow(listOf())

    fun getDrivers()  {
        repo.getDrivers(driversFlow = driversFlow, routesFlow = routesFlow)
    }


    fun findDriver(driverId: String): Driver? {
        // TODO: return a specific route based on business rules
        //for now just punt
//        return _drivers.firstOrNull { it.id == driverId }
        _drivers.value?.forEach { driver: Driver ->
            if (driver.id == driverId) return driver
        }
        return null
    }


    fun findRoute(driverId: String): Route {
        //TODO actually implement this using business rules from problem domain
        return Route(id= 0, name = "Route Zero", type = "I")
    }


    fun setDrivers() {
        if (isSorted) setSortByDriverId()
        else setUnsortedDrivers()
    }

    fun setUnsortedDrivers() {
        //just get remote drivers again
        getDrivers()
    }

    fun setSortByDriverId() {
        //TODO this sorts 18 as lower than 2, so need to fix the sorting
        // see https://stackoverflow.com/questions/69956811/kotlin-sort-listt-with-t-being-a-class
        val sortedList = _drivers.value?.sortedBy { driver: Driver -> driver.id } ?: listOf()
        _drivers.value = sortedList
    }

    fun printDrivers() {
        drivers.value?.forEach { driver ->
            println("DriverID: ${driver.id} is named: ${driver.name} " )
        }
    }
}