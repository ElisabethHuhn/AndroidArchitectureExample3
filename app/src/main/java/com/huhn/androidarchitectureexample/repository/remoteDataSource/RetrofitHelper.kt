package com.huhn.androidarchitectureexample.repository.remoteDataSource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    // assure that this baseurl ends with "/"
    private const val BASE_URL = "https://d49c3a78-a4f2-437d-bf72-569334dea17c.mock.pstmn.io/"

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            //define where to get the Json
            .baseUrl(BASE_URL)
            // convert JSON object to Kotlin object
            .addConverterFactory(GsonConverterFactory.create())
            //build the Retrofit instance
            .build()
    }

    val driverApi: DriverApiService = getInstance().create(DriverApiService::class.java)

}
