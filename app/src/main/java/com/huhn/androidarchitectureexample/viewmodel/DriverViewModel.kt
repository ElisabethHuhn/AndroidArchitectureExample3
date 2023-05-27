package com.huhn.androidarchitectureexample.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huhn.androidarchitectureexample.repository.DriverRepositoryImpl
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverViewModel(private val repo : DriverRepositoryImpl) : ViewModel()
{
    var isSorted = false
    var drivers = MutableLiveData<List<Driver>>()
    var routes = MutableLiveData<List<Route>>()


    fun getDrivers()  {
        repo.getDrivers(driverCallbackHandler)
    }

    fun findDriver(driverId: String): Driver? {
        // TODO: return a specific route based on business rules
        //for now just punt
//        return _drivers.firstOrNull { it.id == driverId }
        drivers.value?.forEach { driver: Driver ->
            if (driver.id == driverId) return driver
        }
        return null
    }


    fun findRoute(driverId: String): Route? {
        //TODO actually implement this using business rules from problem domain
        return Route(id= 0, name = "Route Zero", type = "I")
    }

    //Create the callback lambda function that will parse the response and
    // actually fill the drivers and routes variables
    //Define the code to execute upon request return
    val driverCallbackHandler = object : Callback<DriverResponse> {
        val TAG = "driverCallBackHandler"

        override fun onFailure(call: Call<DriverResponse>, t: Throwable) {
            Log.e(TAG, "Failure Return from network call", t)
            // TODO: Somehow inform the user of the error
        }

        override fun onResponse(
            call: Call<DriverResponse>,
            response: Response<DriverResponse>
        ) {
            Log.d(TAG, "Response Received")

            try {
                val responseCode = response.code()
                // debug information
                val responseMessage = response.message()
                val responseIsSuccessful = response.isSuccessful
                val responseHeaders = response.headers()
                val responseErrorBody = response.errorBody()
                val responseDebug =
                    "code = $responseCode, isSuccessful = $responseIsSuccessful, message = $responseMessage, headers = $responseHeaders, error = $responseErrorBody"
                Log.d(TAG, responseDebug)
                // end debug info

                when (responseCode) {
                    200 -> {
                        drivers.value = response.body()?.drivers ?: listOf()
                        routes.value = response.body()?.routes ?: listOf()
                    }
                    else -> {
                        // TODO: inform the user about the error
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Exception caught accessing response ${e.message}")
            }
        }
    }


    fun getUnsortedDrivers(): List<Driver> {
        return drivers.value ?: listOf()
    }

    fun getSortedDrivers() : List<Driver> {
        //TODO implement a sort by last name
        //for now, cheat
        val unsortedDrivers = drivers.value

        if (unsortedDrivers == null) return listOf()
        return listOf(
            unsortedDrivers[10],
            unsortedDrivers[9],
            unsortedDrivers[8],
            unsortedDrivers[7],
            unsortedDrivers[6],
            unsortedDrivers[5],
            unsortedDrivers[4],
            unsortedDrivers[3],
            unsortedDrivers[2],
            unsortedDrivers[1],
            unsortedDrivers[0],
        )
    }

}