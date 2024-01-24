package com.huhn.androidarchitectureexample.repository.remoteDataSource

// Retrofit interface
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Dates
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.EnhancedDateItem
import retrofit2.http.GET
import retrofit2.http.Path

interface DatesApiService {
    @GET("natural/all")
    suspend fun fetchDates() : ArrayList<Dates>

    @GET("natural/date/{dateString}")
    suspend fun fetchEnhancedDates(@Path("dateString") date: String) : ArrayList<EnhancedDateItem>
}
