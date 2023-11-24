@file:Suppress("IllegalIdentifier")

package com.huhn.androidarchitectureexample.repository

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.huhn.androidarchitectureexample.TestDriverDataImpl
import com.huhn.androidarchitectureexample.dependencyInjection.BASE_URL
import com.huhn.androidarchitectureexample.repository.localDataSource.AppDatabase
import com.huhn.androidarchitectureexample.repository.remoteDataSource.DriverApiService
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.DriverResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(AndroidJUnit4::class)
@SmallTest
class DriverRepositoryTest {

    private lateinit var driverApiService: DriverApiService

    @Before
    fun setup() {
        driverApiService = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(DriverApiService::class.java)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.huhn.androidarchitectureexample", appContext.packageName)
    }

    //arrange, act assert

    @Test
    fun testLocalDb() = runTest {
        //arrange
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.databaseBuilder(context = appContext,
            klass = AppDatabase::class.java, "aae-database"
        ).build()
        val dbDriverDao = db.dbDriverDao()
        val dbRouteDao = db.dbRouteDao()

        val repository = DriverRepositoryImpl(
            dbDriverDao = dbDriverDao,
            dbRouteDao = dbRouteDao,
            driverApiService = driverApiService
        )


        var expected = DriverResponse(listOf(), listOf())
        repository.deleteAllDriversAndRoutesLocal()

        //act
        var actual = repository.getDriversAndRoutesLocal(
            isSorted = false,
            dbScope = this
        )
        //assert
        Assert.assertEquals("Clearing DB Failed", expected,  actual)

        expected = TestDriverDataImpl.defaultDriverResponse()

        repository.insertDriversAndRoutesIntoDb(
            driverResponse = expected,
            dbScope = this
        )
        //act
        actual = repository.getDriversAndRoutesLocal(
            isSorted = true,
            dbScope = this
        )

        //assert
        Assert.assertEquals("Inserting data into DB Failed", expected,  actual)

        //act
        actual = repository.getDriversAndRoutesLocal(
            isSorted = false,
            dbScope = this
        )

        //assert
        Assert.assertNotEquals("Resort Failed", expected,  actual)


        //arrange
        expected = TestDriverDataImpl.emptyDriverResponse()

        //act
        repository.deleteAllDriversAndRoutesLocal()
        actual = repository.getDriversAndRoutesLocal(
            isSorted = true,
            dbScope = this
        )

        //assert
        Assert.assertEquals("Clearing DB Failed", expected,  actual)
    }

    @Test
    fun testGetRemote() = runTest {
        //arrange
        //arrange
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.databaseBuilder(context = appContext,
            klass = AppDatabase::class.java, "aae-database"
        ).build()
        val dbDriverDao = db.dbDriverDao()
        val dbRouteDao = db.dbRouteDao()

        val repository = DriverRepositoryImpl(
            dbDriverDao = dbDriverDao,
            dbRouteDao = dbRouteDao,
            driverApiService = driverApiService
        )


        try {
            //act
           val actual = repository.getDriversAndRoutesRemote(
                isSorted = false,
                dbScope = this
            )

            //assert
            assert(actual.drivers.isNotEmpty())
            assert(actual.routes.isNotEmpty())

        } catch (e: Exception) {
            assert(false)
        }
    }

}