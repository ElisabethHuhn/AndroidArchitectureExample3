package com.huhn.androidarchitectureexample.viewmodel

import androidx.lifecycle.ViewModel
import com.huhn.androidarchitectureexample.repository.DriverRepositoryImpl
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver

class DriverViewModel(private val repo : DriverRepositoryImpl) : ViewModel()
{
    var isSorted = false

    fun getDrivers() : List<Driver> {
        if (isSorted) return repo.getDrivers()
        else return repo.getUnsortedDrivers()
    }

    fun findDriver(driverId: String) : Driver? {
        return repo.findDriver(driverId = driverId)
    }
}