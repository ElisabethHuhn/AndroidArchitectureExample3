package com.huhn.androidarchitectureexample.repository

import com.huhn.androidarchitectureexample.repository.localDataSource.AppDatabase
import com.huhn.androidarchitectureexample.repository.localDataSource.DBDriverDao
import com.huhn.androidarchitectureexample.repository.remoteDataSource.DriverApiService
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
        val baseUrl = "https://d49c3a78-a4f2-437d-bf72-569334dea17c.mock.pstmn.io"

        //Instantiate Retrofit
        val retrofitInstance: Retrofit =
            Retrofit.Builder()
                //define where to get the Json
                .baseUrl(baseUrl)
                // convert JSON object to Kotlin object
                .addConverterFactory(GsonConverterFactory.create())
                //build the Retrofit instance
                .build()

        driverApi = retrofitInstance.create(DriverApiService::class.java)

    }

    override fun getDrivers(driverCallbackHandler: Callback<DriverResponse>) {
        //TODO this should hide whether the driver data is coming from remote or local
        //for now, remote data only, and the callback updates the ViewModel LiveData
        //The Compose UI will recompose when the viewmodel.drivers list changes
        //TODO investigate setting up a Flow in the callback, and updating the viewmodel that way
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            val driversAndRoutesCall = driverApi.fetchDriverResponseCall()
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