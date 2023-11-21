package com.huhn.androidarchitectureexample.dependencyInjection

import androidx.room.Room
import com.huhn.androidarchitectureexample.viewmodel.DriverViewModel
import com.huhn.androidarchitectureexample.repository.DriverRepositoryImpl
import com.huhn.androidarchitectureexample.repository.localDataSource.AppDatabase
import com.huhn.androidarchitectureexample.repository.remoteDataSource.DriverApiService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
 * Koin needs:
 * o A dependency in the build.gradle(app) file
 * o to be started in the Application class
 * o koin module(s) defining how classes are to be created
 */

//Note the use of Constructor DSL rather than the older single<>{  } syntax
const val BASE_URL = "https://d49c3a78-a4f2-437d-bf72-569334dea17c.mock.pstmn.io/"
val koinModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "aae-database"
        ).build()
    }
    single {
        get<AppDatabase>().dbDriverDao()
    }
    single {
        get<AppDatabase>().dbRouteDao()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DriverApiService::class.java)
    }

    singleOf(::DriverRepositoryImpl)
    viewModelOf(::DriverViewModel)
}