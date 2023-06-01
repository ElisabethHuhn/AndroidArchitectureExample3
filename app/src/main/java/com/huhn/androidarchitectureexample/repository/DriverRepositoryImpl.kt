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
                   routesFlow: MutableStateFlow<List<Route>>,
                   isSorted: Boolean
    )
    fun findDriver(driverId: String) : Driver?

//    fun findRoute(driverId: String) : Route?
}


class DriverRepositoryImpl : DriverRepository {
    //local data source variables
    private var db: AppDatabase
    private var dbDriverDao: DBDriverDao
    private var dbRouteDao: DBRouteDao

    //remote data source variables
    private val driverApi: DriverApiService

    lateinit var driversFlow: MutableStateFlow<List<Driver>>

    lateinit var routesFlow: MutableStateFlow<List<Route>>

    var saveisSorted = false

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
         routesFlow: MutableStateFlow<List<Route>>,
         isSorted: Boolean,
     ) {
         //Save the flows so that the Retrofit handler can use them to return the lists to the ViewModel
         this.driversFlow = driversFlow
         this.routesFlow = routesFlow
         saveisSorted = isSorted

        //TODO this should hide whether the driver data is coming from remote or local
        //for now, remote data only, and the callback updates the ViewModel LiveData
        //The Compose UI will recompose when the view-model.drivers list changes
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val drivers =  getDriversLocal(isSorted = isSorted)
            val routes =  getRoutesLocal()
            if (drivers.isEmpty()) {
                saveisSorted = isSorted
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

    private suspend fun getDriversLocal(isSorted: Boolean) : List<Driver> {
        val scope = CoroutineScope(Dispatchers.IO)
        var drivers : List<DBDriver> = listOf()
        val job = scope.launch {
            drivers = if (isSorted) {
                dbDriverDao.sortDriverByAscId()
            } else {
                dbDriverDao.getAllDrivers()
            }
        }
        job.join()
        //convert DB drivers into Driver drivers
        return drivers.map { dbDriver: DBDriver ->
            Driver(
                id = dbDriver.uid.toString(),
                name = dbDriver.name ?: "",
            )
        }
    }
    private suspend fun getRoutesLocal() : List<Route> {
        val scope = CoroutineScope(Dispatchers.IO)
        var routes : List<DBRoute> = listOf()
        val job = scope.launch {
            routes = dbRouteDao.getAllRoutes()
        }
        job.join()
        //convert DB drivers into Driver drivers
        return routes.map {dbRoute : DBRoute ->
            Route(
                id = dbRoute.uid,
                name = dbRoute.name ?: "",
                type = dbRoute.type ?: ""
            )
        }
    }
    private fun getDriversRemote(driverRemoteCallbackHandler: Callback<DriverResponse>) {

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val driversAndRoutesCall = driverApi.fetchDriverResponseCall()
            //Initiate the remote call, with the passed callback to handle the remote response
            driversAndRoutesCall.enqueue(driverRemoteCallbackHandler)
        }
    }

    override fun findDriver(driverId: String) : Driver? {
        val driverUid: Int
        try {
            driverUid = driverId.toInt()
        } catch (e: Exception) {
            Log.e("DriverRepository: ", "Bad DriverId: $driverId in findDriver")
            return null
        }
        // TODO: Figure out how to job.join more elegantly
        var driver: Driver? = null

        val outerScope = CoroutineScope(Dispatchers.IO)
        val outerJob = outerScope.launch {
            val scope = CoroutineScope(Dispatchers.IO)
            val job = scope.launch {
                driver = findDriverLocal(driverUid)
                if (driver == null) {
                    // TODO: Need to figure out how to make remote drivers call
                    Log.e("FindDriver", "No local drivers")

                }
            }
            job.join()
        }

        return driver
    }

    private suspend fun findDriverLocal(driverUid: Int) : Driver? {
        val scope = CoroutineScope(Dispatchers.IO)
        var dbDriver : DBDriver? = null
        val job = scope.launch {
            dbDriver = dbDriverDao.findDriversById(driverUid)
        }
        job.join()

        return dbDriver?.let {
            Driver(
                id = dbDriver!!.uid.toString(),
                name = dbDriver!!.name ?: "",
            )
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
                        var driversList = response.body()?.drivers ?: listOf()
                        var routesList = response.body()?.routes ?: listOf()
                        //insert the list of drivers into the local DB
                        val outerScope = CoroutineScope(Dispatchers.IO)
                        val outerJob = outerScope.launch {
                            val scope = CoroutineScope(Dispatchers.IO)
                            val job = scope.launch {
                                val dbDriverList = driversList.map { driver: Driver ->
                                    DBDriver(
                                        uid = driver.id.toInt(),
                                        name = driver.name
                                    )
                                }
                                dbDriverDao.insertAllDrivers(*dbDriverList.toTypedArray())
                                driversList = getDriversLocal(isSorted = saveisSorted)
                                routesList = getRoutesLocal()
                            }
                            job.join()
                        }

                        returnDriversToViewModel(
                            drivers = driversList,
                            routes = routesList
                        )
                    }
                    else -> {
                        // TODO: inform the user about the error
                        Log.e(TAG, "Error response code received on API call")
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

    private fun createDb() :  AppDatabase{
        val appContext = AndroidArchitectureExampleApplication.appContext

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "aae-database"
        ).build()
    }
}