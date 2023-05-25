package com.huhn.androidarchitectureexample.repository.remoteDataSource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val baseUrl = "https://d49c3a78-a4f2-437d-bf72-569334dea17c.mock.pstmn.io"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            //define where to get the Json
            .baseUrl(baseUrl)
            // convert JSON object to Kotlin object
            .addConverterFactory(GsonConverterFactory.create())
            //build the Retrofit instance
            .build()
    }
}

    //Retrofit summary
//val retrofit = Retrofit.Builder()
//    .baseUrl("https://api.example.com/")
//    .addConverterFactory(GsonConverterFactory.create())
//    .build()
//
//val apiService = retrofit.create(ApiService::class.java)
//val call = apiService.getUser(userId)
//call.enqueue(object : Callback<User> {
//    override fun onResponse(call: Call<User>, response: Response<User>) {
//        // Handle the response
//    }
//
//    override fun onFailure(call: Call<User>, t: Throwable) {
//        // Handle the failure
//    }
//})