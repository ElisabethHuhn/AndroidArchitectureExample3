package com.huhn.androidarchitectureexample.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.huhn.androidarchitectureexample.repository.database.AppDatabase
import com.huhn.androidarchitectureexample.repository.database.DBDriverDao
import com.huhn.androidarchitectureexample.repository.network.DriverApi
import com.huhn.androidarchitectureexample.repository.network.RetrofitHelper
import com.huhn.androidarchitectureexample.repository.network.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.network.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.network.networkModel.Route
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(val context: Context) {
    private lateinit var dbDriverDao: DBDriverDao
    private lateinit var db: AppDatabase
    private val driverApi : DriverApi

    var drivers: List<Driver> = listOf()
    var routes: List<Route> = listOf()

    init {
        val baseUrl = "https://d49c3a78-a4f2-437d-bf72-569334dea17c.mock.pstmn.io"

        //Instantiate Retrofit
        val retrofitInstance: Retrofit =
             Retrofit.Builder()
                //define where to get the Json
                .baseUrl(RetrofitHelper.baseUrl)
                // convert JSON object to Kotlin object
                .addConverterFactory(GsonConverterFactory.create())
                //build the Retrofit instance
                .build()

        driverApi = RetrofitHelper.getInstance().create(DriverApi::class.java)

    }

    //call this from the ViewModel using the viewModel scope
    suspend fun getDrivers() = withContext(Dispatchers.IO) {
        //first check if the local DB has the driver list
        //if it does, return the list of drivers from there
        // TODO: implement ROOM

        //if not, get them from the server
        val result = fetchDrivers()

        // debug logging the results
        Log.d("huhn RetrofitResponse: ", result.value.toString())

        drivers
    }


    private val TAG = "Respository.fetchDrivers()"
    //call this from getDrivers() if the DB is empty
    suspend fun fetchDrivers(): MutableLiveData<DriverResponse> {
        val responseLiveData: MutableLiveData<DriverResponse> = MutableLiveData()

        //use the api instance to create the web request which will be executed later
        val driverRequest : Call<DriverResponse> = driverApi.fetchDriverResults()

        //Define the code to execute upon request return
        val callbackHandler = object : Callback<DriverResponse> {
            override fun onFailure(call: Call<DriverResponse>, t: Throwable) {
                Log.e(TAG, "Failure Return from network call", t)
                // TODO: Somehow inform the user of the error
            }

            override fun onResponse(call: Call<DriverResponse>, response: Response<DriverResponse>) {
                Log.d(TAG, "Response Received")

                try {
                    val responseCode = response.code()
                    // debug information
                    val responseMessage = response.message()
                    val responseIsSuccessful = response.isSuccessful
                    val responseHeaders = response.headers()
                    val responseErrorBody = response.errorBody()
                    val responseDebug = "code = $responseCode, isSuccessful = $responseIsSuccessful, message = $responseMessage, headers = $responseHeaders, error = $responseErrorBody"
                    Log.d(TAG, responseDebug)
                    // end debug info

                    when (responseCode) {
                        200 -> {
                            drivers = response.body()?.drivers ?: listOf()
                            routes = response.body()?.routes ?: listOf()
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



    // TODO: need to get context from DI Koin
    fun createDb() {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "aae-database"
        ).build()
        dbDriverDao = db.dbDriverDao()
    }
}