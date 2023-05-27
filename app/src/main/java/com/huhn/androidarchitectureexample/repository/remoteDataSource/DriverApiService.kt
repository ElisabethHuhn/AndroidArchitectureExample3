package com.huhn.androidarchitectureexample.repository.remoteDataSource

// Retrofit interface
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import retrofit2.Call
import retrofit2.http.GET


// TODO: fix the get URI
interface DriverApiService {
    @GET("/data")
    fun fetchDriverResponseCall() : Call<DriverResponse>
}
//
//interface DriverApiHelper {
//    suspend fun fetchDriverResponse() : Call<DriverResponse>
//}
//
//class DriverApiHelperImpl(private val driverApiService: DriverApiService) : DriverApiHelper {
//    override suspend fun fetchDriverResponse() = driverApiService.fetchDriverResponse()
//}
