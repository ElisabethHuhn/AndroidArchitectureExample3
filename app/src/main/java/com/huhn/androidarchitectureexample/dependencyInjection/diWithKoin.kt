package com.huhn.androidarchitectureexample.dependencyInjection

import com.huhn.androidarchitectureexample.repository.DriverRepositoryImpl
import com.huhn.androidarchitectureexample.viewmodel.DriverViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/*
 * Koin needs:
 * o A dependency in the build.gradle(app) file
 * o to be started in the Application class
 * o koin module(s) defining how classes are to be created
 */

//Note the use of Constructor DSL rather than the older single<>{  } syntax
val koinModule = module {
    singleOf(::DriverRepositoryImpl)
    viewModelOf(::DriverViewModel)
}