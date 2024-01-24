package com.huhn.androidarchitectureexample.dependencyInjection

import com.huhn.androidarchitectureexample.repository.remoteDataSource.DatesApiService
import com.huhn.androidarchitectureexample.viewmodel.DatesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
 * Koin needs:
 * o A dependency in the build.gradle(app) file
 * o to be started in the Application class
 * o Include the application class in the manifest
 * o koin module(s) defining how classes are to be created
 */

const val BASE_URL = "https://epic.gsfc.nasa.gov/api/"
val koinModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DatesApiService::class.java)
    }


    viewModel {
        DatesViewModel(get())
    }

//
//    singleOf(::DatesRepositoryImpl)
//    viewModelOf(::DatesViewModel)
}