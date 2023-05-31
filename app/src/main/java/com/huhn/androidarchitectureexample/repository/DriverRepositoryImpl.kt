package com.huhn.androidarchitectureexample.repository

import android.util.Log
import androidx.room.Room
import com.huhn.androidarchitectureexample.application.AndroidArchitectureExampleApplication
import com.huhn.androidarchitectureexample.repository.localDataSource.AppDatabase
import com.huhn.androidarchitectureexample.repository.localDataSource.DBDriverDao
import com.huhn.androidarchitectureexample.repository.localDataSource.DBRouteDao
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBDriver
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBRoute
import com.huhn.androidarchitectureexample.repository.remoteDataSource.DriverApiService
import com.huhn.androidarchitectureexample.repository.remoteDataSource.RetrofitHelper
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface DriverRepository {

    fun getDrivers(driversFlow: MutableStateFlow<List<Driver>>,
                   routesFlow: MutableStateFlow<List<Route>>)
//    fun findDriver(driverId: String) : Driver?
//
//    fun findRoute(driverId: String) : Route?
}


class DriverRepositoryImpl : DriverRepository {
    //local data source variables
    private var db: AppDatabase
    private var dbDriverDao: DBDriverDao
    private var dbRouteDao: DBRouteDao

    //remote data source variables
    private val driverApi: DriverApiService

    //local cache of drivers and routes
//    var _drivers = MutableLiveData<List<Driver>>()
//    val drivers : LiveData<List<Driver>>
//        get() = _drivers
    lateinit var driversFlow: MutableStateFlow<List<Driver>>

//    var _routes = MutableLiveData<List<Route>>()
//    val routes : MutableLiveData<List<Route>>
//        get() = _routes
    lateinit var routesFlow: MutableStateFlow<List<Route>>


    init {
        /*
         * Create the DAOs corresponding to the db tables
         */
        db = createDb()
        dbDriverDao = db.dbDriverDao()
        dbDriverDao = db.dbDriverDao()
        dbRouteDao = db.dbRouteDao()

        /*
         * Use RetrofitHelper to create the instance of Retrofit
         * Then use this instance to create an instance of the API
         */
        driverApi = RetrofitHelper.getInstance().create(DriverApiService::class.java)
    }

     override fun getDrivers(
         driversFlow: MutableStateFlow<List<Driver>>,
         routesFlow: MutableStateFlow<List<Route>>
     ) {
         //Save the flows so that the Retrofit handler can use them to return the lists to the ViewModel
         this.driversFlow = driversFlow
         this.routesFlow = routesFlow

        //TODO this should hide whether the driver data is coming from remote or local
        //for now, remote data only, and the callback updates the ViewModel LiveData
        //The Compose UI will recompose when the view-model.drivers list changes
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val drivers =  getDriversLocal()
            val routes =  getRoutesLocal()
            if (drivers.isEmpty()) {
                getDriversRemote(
                    driverRemoteCallbackHandler = driverRemoteCallbackHandler
                )
            } else {
                returnDriversToViewModel(
                    drivers = drivers,
                    routes = routes
                )
            }
        }
    }

    suspend fun getDriversLocal() : List<Driver> {
        val scope = CoroutineScope(Dispatchers.IO)
        var drivers : List<DBDriver> = listOf()
        val job = scope.launch {
            drivers = dbDriverDao.getAllDrivers()
        }
        job.join()
        //convert DB drivers into Driver drivers
        val returnDrivers : MutableList<Driver> = mutableListOf()
        drivers.forEach { dbDriver: DBDriver ->
            val newDriver = Driver(
                id = dbDriver.uid.toString(),
                name = dbDriver.name ?: "",
            )
            returnDrivers.add(newDriver)
        }
        return returnDrivers.toList()
    }
    suspend fun getRoutesLocal() : List<Route> {
        val scope = CoroutineScope(Dispatchers.IO)
        var routes : List<DBRoute> = listOf()
        val job = scope.launch {
            routes = dbRouteDao.getAllRoutes()
        }
        job.join()
        //convert DB drivers into Driver drivers
        val returnRoutes : MutableList<Route> = mutableListOf()
        routes.forEach { dbRoute : DBRoute ->
            val newRoute = Route(
                id = dbRoute.uid,
                name = dbRoute.name ?: "",
                type = dbRoute.type ?: ""
            )
            returnRoutes.add(newRoute)
        }
        return returnRoutes.toList()
    }


    fun getDriversRemote(driverRemoteCallbackHandler: Callback<DriverResponse>) {

        //TODO investigate setting up a Flow in the callback, and updating the view-model that way
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val driversAndRoutesCall = driverApi.fetchDriverResponseCall()
            //Initiate the remote call, with the passed callback to handle the remote response
            driversAndRoutesCall.enqueue(driverRemoteCallbackHandler)
        }
    }

    //Create the callback object that will parse the response and
    // actually fill the drivers and routes variables
    //Define the code to execute upon request return
    val driverRemoteCallbackHandler = object : Callback<DriverResponse> {
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
//                        _drivers.value = response.body()?.drivers ?: listOf()
//                        _routes.value = response.body()?.routes ?: listOf()
                        returnDriversToViewModel(
                            drivers = response.body()?.drivers ?: listOf(),
                            routes = response.body()?.routes ?: listOf()
                        )
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

    fun returnDriversToViewModel(drivers: List<Driver>, routes: List<Route>){
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            driversFlow.emit(drivers)
            routesFlow.emit(routes)
        }
    }

    fun createDb() :  AppDatabase{
        val appContext = AndroidArchitectureExampleApplication.appContext

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "aae-database"
        ).build()
    }
}