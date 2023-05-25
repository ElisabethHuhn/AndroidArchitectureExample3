package com.huhn.androidarchitectureexample.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.huhn.androidarchitectureexample.repository.localDataSource.AppDatabase
import com.huhn.androidarchitectureexample.repository.localDataSource.DBDriverDao
import com.huhn.androidarchitectureexample.repository.remoteDataSource.DriverApi
import com.huhn.androidarchitectureexample.repository.remoteDataSource.RetrofitHelper
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface DriverRepository {
    fun getDrivers() : List<Driver>
    fun findDriver(driverId: String) : Driver?

    fun findRoute(driverId: String) : Route?
}

class DriverRepositoryImpl() : DriverRepository {
    //local data source variables
    private lateinit var dbDriverDao: DBDriverDao
    private lateinit var db: AppDatabase

    //remote data source variables
    private val driverApi: DriverApi

    //todo ultimately, this needs to go away, and only return results of query of local data source
    private var _drivers = listOf<Driver>()

    override fun getDrivers(): List<Driver> {
        //TODO first check local data source, then check remote data source
        val scope = CoroutineScope(Dispatchers.IO)
        //todo decide whether we need this local variable
        val localDrivers = scope.launch {
            _drivers = getRemoteDrivers()
        }
        return _drivers
    }

    override fun findDriver(driverId: String): Driver? {
        // TODO: return a specific route based on business rules
        //for now just punt
//        return _drivers.firstOrNull { it.id == driverId }
        getSortedDrivers().forEach { driver: Driver ->
            if (driver.id == driverId) return driver
        }
        return null
    }


    private var _routes: List<Route> = listOf()
    override fun findRoute(driverId: String): Route? {
        //TODO actually implement this using business rules from problem domain
        return Route(id= 0, name = "Route Zero", type = "I")
    }


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

        driverApi = RetrofitHelper.getInstance().create(DriverApi::class.java)

    }

    //call this from the ViewModel using the viewModel scope
    suspend fun getRemoteDrivers() = withContext(Dispatchers.IO) {
        //first check if the local DB has the driver list
        //if it does, return the list of drivers from there
        // TODO: implement ROOM

        //if not, get them from the server
        val result = fetchDrivers()

        // debug logging the results
        Log.d("huhn RetrofitResponse: ", result.value.toString())

        _drivers//return the list of drivers
    }


    private val TAG = "Respository.fetchDrivers()"

    //call this from getDrivers() if the DB is empty
    fun fetchDrivers(): MutableLiveData<DriverResponse> {
        val responseLiveData: MutableLiveData<DriverResponse> = MutableLiveData()

        //use the api instance to create the web request which will be executed later
        val driverRequest: Call<DriverResponse> = driverApi.fetchDriverResults()

        //Define the code to execute upon request return
        val callbackHandler = object : Callback<DriverResponse> {
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
                            _drivers = response.body()?.drivers ?: listOf()
                            _routes = response.body()?.routes ?: listOf()
                        }

                        else -> {
                            // TODO: inform the user about the error
                        }
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "Exception caught accessing response ${e.message}")
                }

                responseLiveData.value = response.body()
            }
        }
        //enqueue the call request to Retrofit
        driverRequest.enqueue(callbackHandler)

        return responseLiveData
    }
//
    //The following were the original test functions
    //
    //    fun getUnsortedDrivers() = listOf<Driver>(
//        Driver(id ="0", name = "Driver Zero"),
//        Driver(id ="1", name = "Driver One"),
//        Driver(id ="2", name = "Driver Two"),
//        Driver(id ="3", name = "Driver Three"),
//        Driver(id ="4", name = "Driver Four"),
//        Driver(id ="5", name = "Driver Five"),
//        Driver(id ="6", name = "Driver Six"),
//        Driver(id ="7", name = "Driver Seven"),
//        Driver(id ="8", name = "Driver Eight"),
//        Driver(id ="9", name = "Driver Nine"),
//        Driver(id ="10", name = "Driver Ten"),
//    )
//    fun getSortedDrivers() : List<Driver> {
//        //implement a sort by last name
//        //for now, cheat
//        val drivers = getUnsortedDrivers()
//
//        return listOf(
//            drivers[10],
//            drivers[9],
//            drivers[8],
//            drivers[7],
//            drivers[6],
//            drivers[5],
//            drivers[4],
//            drivers[3],
//            drivers[2],
//            drivers[1],
//            drivers[0],
//        )
//    }
    fun getUnsortedDrivers(): List<Driver> {
        return getDrivers()
    }

    fun getSortedDrivers() : List<Driver> {
        //TODO implement a sort by last name
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