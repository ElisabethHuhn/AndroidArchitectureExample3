package com.huhn.androidarchitectureexample.repository

import com.huhn.androidarchitectureexample.repository.localDataSource.AppDatabase
import com.huhn.androidarchitectureexample.repository.localDataSource.DBDriverDao
import com.huhn.androidarchitectureexample.repository.remoteDataSource.DriverApiService
import com.huhn.androidarchitectureexample.repository.remoteDataSource.RetrofitHelper
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface DriverRepository {
    fun getDrivers(driverCallbackHandler: Callback<DriverResponse>)
//    fun findDriver(driverId: String) : Driver?
//
//    fun findRoute(driverId: String) : Route?
}

class DriverRepositoryImpl() : DriverRepository {
    //local data source variables
    private lateinit var dbDriverDao: DBDriverDao
    private lateinit var db: AppDatabase

    /*
     * use init block to create RetroFit instance and DriverApiService instance
     * we'll use the driverApi instance to make RetroFit Calls
     */

    //remote data source variables
    private val driverApi: DriverApiService

    init {
        /*
         * Use RetrofitHelper to create the instance of Retrofit
         * Then use this instance to create an instance of the API
         */
        driverApi = RetrofitHelper.getInstance().create(DriverApiService::class.java)
    }

    override fun getDrivers(driverCallbackHandler: Callback<DriverResponse>) {
        //TODO this should hide whether the driver data is coming from remote or local
        //for now, remote data only, and the callback updates the ViewModel LiveData
        //The Compose UI will recompose when the view-model.drivers list changes
        //TODO investigate setting up a Flow in the callback, and updating the view-model that way
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            val driversAndRoutesCall = driverApi.fetchDriverResponseCall()
            //Initiate the remote call, with the passed callback to handle the remote response
            driversAndRoutesCall.enqueue(driverCallbackHandler)
        }
    }



//
//    // TODO: need to get context from DI Koin
//    fun createDb() {
//        db = Room.databaseBuilder(
//            context,
//            AppDatabase::class.java, "aae-database"
//        ).build()
//        dbDriverDao = db.dbDriverDao()
//    }
}