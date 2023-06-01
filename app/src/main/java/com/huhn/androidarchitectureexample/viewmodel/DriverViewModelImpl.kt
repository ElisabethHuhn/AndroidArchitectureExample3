package com.huhn.androidarchitectureexample.viewmodel

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

    val driversFlow : MutableStateFlow<List<Driver>> = MutableStateFlow(listOf())

    val routesFlow : MutableStateFlow<List<Route>> = MutableStateFlow(listOf())

    fun getDrivers()  {
        repo.getDrivers(
            driversFlow = driversFlow,
            routesFlow = routesFlow,
            isSorted = isSorted
        )
    }


    fun findDriver(driverId: String): Driver? {
        // TODO: return a specific route based on business rules
        //for now just punt
        return repo.findDriver(driverId = driverId)
    }


    fun findRoute(driverId: String): Route {
        //TODO actually implement this using business rules from problem domain
        return Route(id= 0, name = "Route Zero", type = "I")
    }


}