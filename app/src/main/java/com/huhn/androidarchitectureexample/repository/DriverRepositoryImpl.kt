package com.huhn.androidarchitectureexample.repository

import androidx.room.Room
import com.huhn.androidarchitectureexample.application.AndroidArchitectureExampleApplication
import com.huhn.androidarchitectureexample.repository.localDataSource.AppDatabase
import com.huhn.androidarchitectureexample.repository.localDataSource.DBDriverDao
import com.huhn.androidarchitectureexample.repository.localDataSource.DBRouteDao
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBDriver
import com.huhn.androidarchitectureexample.repository.localDataSource.dbModel.DBRoute
import com.huhn.androidarchitectureexample.repository.remoteDataSource.RetrofitHelper
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

interface DriverRepository {
    suspend fun fetchDriverLists(isSorted: Boolean, scope: CoroutineScope) : DriverResponse
}


class DriverRepositoryImpl : DriverRepository {
    //local data source variables
    private var db: AppDatabase
    private var dbDriverDao: DBDriverDao
    private var dbRouteDao: DBRouteDao

    init {
        /*
         * Create the DAOs corresponding to the db tables
         */
        db = createDb()
        dbDriverDao = db.dbDriverDao()
        dbDriverDao = db.dbDriverDao()
        dbRouteDao = db.dbRouteDao()

        /*
         * Use RetrofitHelper to create the instance of Retrofit
         * Then use this instance to create an instance of the API
         */
//        driverApi = RetrofitHelper.getInstance().create(DriverApiService::class.java)
    }

     override suspend fun fetchDriverLists(
         isSorted: Boolean,
         scope: CoroutineScope
     ) : DriverResponse {
         var returnLists = getDriversAndRoutesLocal(
             isSorted = isSorted,
             dbScope = scope
             )

         if (returnLists.drivers.isEmpty() || returnLists.routes.isEmpty()) {
            returnLists = getDriversRemote(
                isSorted = isSorted,
                dbScope = scope
                )
         }

         return returnLists
     }

    private suspend fun getDriversAndRoutesLocal(
        isSorted: Boolean,
        dbScope: CoroutineScope
    ): DriverResponse {
        var driversList : List<Driver> = listOf()
        var routesList : List<Route> = listOf()

        //insert the drivers and routes into the local DB in parallel
        //so we need new coroutines for both

        val driverJob = dbScope.launch {
            //insert the list of drivers into the local DB
            //read the list of drivers back from the local DB
            driversList = getDriversLocal(isSorted = isSorted)
        }
        val routeJob = dbScope.launch {
            //insert the list of routes into the local db
            //read the list of routes back from the local db
            routesList = getRoutesLocal()
        }
        //wait until the launched coroutines are complete
        val jobs = listOf(driverJob, routeJob)
        jobs.joinAll()

        return DriverResponse(drivers = driversList, routes = routesList)
    }

    private suspend fun getDriversLocal(isSorted: Boolean) : List<Driver> {
        //get the local drivers
        val dbDrivers = if (isSorted) {
            dbDriverDao.sortDriverByAscId()
        } else {
            dbDriverDao.sortDriverByDescId()
        }

        //convert DB drivers into Driver drivers
        val drivers = dbDrivers.map { dbDriver: DBDriver ->
            Driver(
                id = dbDriver.uid.toString(),
                name = dbDriver.name ?: "",
            )
        }

        return drivers
    }

    private suspend fun getRoutesLocal() : List<Route> {
        //perform these actions serially, so make serial fcn calls
        val dbRoutes = dbRouteDao.getAllRoutes()

        //convert DB drivers into Driver drivers
        val routes = dbRoutes.map { dbRoute: DBRoute ->
            Route(
                id = dbRoute.uid,
                name = dbRoute.name ?: "",
                type = dbRoute.type ?: ""
            )
        }
        return routes
    }

    private suspend fun getDriversRemote(isSorted: Boolean, dbScope: CoroutineScope) : DriverResponse {
        //perform these actions serially, so make serial fcn calls

        //Get the drivers and routes from the remote network call
        var driversResponse = RetrofitHelper.driverApi.fetchDriverResponse()

        //then update the local DB with them
        insertDriversAndRoutesIntoDb(
            driverResponse = driversResponse,
            dbScope = dbScope
        )
        driversResponse = getDriversAndRoutesLocal(
            isSorted = isSorted,
            dbScope = dbScope
        )

        return driversResponse
    }

    private suspend fun insertDriversAndRoutesIntoDb(
        driverResponse: DriverResponse,
        dbScope: CoroutineScope
    ) {
        //insert the drivers and routes into the local DB in parallel
        //so need to launch new coroutines

        val driverJob = dbScope.launch {
            //insert the list of drivers into the local DB
            insertDriver(driverResponse)
        }
        val routeJob = dbScope.launch {
            //insert the list of routes into the local db
            insertRoute(driverResponse)
        }
        //wait until the launched coroutines are complete
        val jobs = listOf(driverJob, routeJob)
        jobs.joinAll()
    }

    private suspend fun insertDriver(driverResponse: DriverResponse) {
        val dbDriverList = driverResponse.drivers.map { dbDriver: Driver ->
            DBDriver(
                uid = dbDriver.id.toInt(),
                name = dbDriver.name
            )
        }
        dbDriverDao.insertAllDrivers(*dbDriverList.toTypedArray())
    }

    private suspend fun insertRoute(driverResponse: DriverResponse) {
        val dbRoutesList = driverResponse.routes.map { route: Route ->
            DBRoute(
                uid = route.id,
                name = route.name,
                type = route.type
            )
        }
        dbRouteDao.insertAllRoutes(*dbRoutesList.toTypedArray())
    }


    suspend fun deleteDriverLocal(driver: Driver)  {
        val dbDriver =  DBDriver(
            uid = driver.id.toInt(),
            name = driver.name
        )
        dbDriver.let {
            dbDriverDao.deleteDriver(dbDriver)
        }
    }

    suspend fun deleteRouteLocal(route: Route)  {
        val dbRoute =  DBRoute(
            uid = route.id,
            name = route.name,
            type = route.type
        )
        dbRoute.let {
            dbRouteDao.deleteRoute(dbRoute)
        }
    }

    private fun createDb() :  AppDatabase{
        val appContext = AndroidArchitectureExampleApplication.appContext

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "aae-database"
        ).build()
    }
}