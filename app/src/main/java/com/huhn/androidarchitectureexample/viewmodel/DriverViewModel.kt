package com.huhn.androidarchitectureexample.viewmodel

import androidx.lifecycle.ViewModel
import com.huhn.androidarchitectureexample.repository.network.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.network.networkModel.Route

class DriverViewModel : ViewModel() {
    var isSorted = false

    fun getDrivers() : List<Driver> {
        if (isSorted) return getSortedDrivers()
        else return getUnsortedDrivers()
    }

    fun getUnsortedDrivers() = listOf<Driver>(
        Driver(id ="0", name = "Driver Zero"),
        Driver(id ="1", name = "Driver One"),
        Driver(id ="2", name = "Driver Two"),
        Driver(id ="3", name = "Driver Three"),
        Driver(id ="4", name = "Driver Four"),
        Driver(id ="5", name = "Driver Five"),
        Driver(id ="6", name = "Driver Six"),
        Driver(id ="7", name = "Driver Seven"),
        Driver(id ="8", name = "Driver Eight"),
        Driver(id ="9", name = "Driver Nine"),
        Driver(id ="10", name = "Driver Ten"),
        )
    fun getSortedDrivers() : List<Driver> {
        //implement a sort by last name
        //for now, cheat
        val drivers = getUnsortedDrivers()

        return listOf(
            drivers[10],
            drivers[9],
            drivers[8],
            drivers[7],
            drivers[6],
            drivers[5],
            drivers[4],
            drivers[3],
            drivers[2],
            drivers[1],
            drivers[0],
            )
    }
    /*
     * Return driver corresponding to DriverID
     */
    fun getDriver(driverId: String): Driver? {
        getSortedDrivers().forEach { driver: Driver ->
            if (driver.id == driverId) return driver
        }
        return null
    }
    fun getRouteFromDriver(driverId: String) : Route {
        return Route(id= 0, name = "Route Zero", type = "I")
    }
}