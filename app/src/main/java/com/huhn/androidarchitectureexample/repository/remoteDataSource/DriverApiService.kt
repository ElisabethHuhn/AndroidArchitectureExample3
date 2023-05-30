package com.huhn.androidarchitectureexample.repository.remoteDataSource

// Retrofit interface
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import retrofit2.Call
import retrofit2.http.GET


interface DriverApiService {
    @GET("/data")
    fun fetchDriverResponseCall() : Call<DriverResponse>
}
