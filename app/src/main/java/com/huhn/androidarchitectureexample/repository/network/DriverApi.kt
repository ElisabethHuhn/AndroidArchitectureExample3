package com.huhn.androidarchitectureexample.repository.network

// Retrofit interface
import com.huhn.androidarchitectureexample.repository.network.networkModel.DriverResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET


// TODO: fix the get URI
interface DriverApi {
    @GET("/data")
    suspend fun fetchDriverResults() : Call<DriverResponse>
}
