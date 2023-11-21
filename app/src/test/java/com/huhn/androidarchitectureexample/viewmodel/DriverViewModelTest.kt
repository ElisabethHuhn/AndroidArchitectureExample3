package com.huhn.androidarchitectureexample.viewmodel

import com.huhn.androidarchitectureexample.ReplaceMainDispatcherRule
import com.huhn.androidarchitectureexample.TestDriverRepositoryImpl
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Driver
import com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test

//@get:Rule
//val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

@OptIn(ExperimentalCoroutinesApi::class)
@get: Rule
val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

class DriverViewModelTest {

    private val repository = TestDriverRepositoryImpl()
    private val driverViewModel = DriverViewModel(repository = repository)

    //arrange, act assert
    @Test
    fun test_toggle_isSorted() {
        //arrange
        val expected = driverViewModel.driverState.value.isSorted
        //act
        driverViewModel.onDriverUserEvent(DriverUserEvent.ToggleIsSorted)
        //assert
        assert(expected != driverViewModel.driverState.value.isSorted)
        //act
        driverViewModel.onDriverUserEvent(DriverUserEvent.ToggleIsSorted)
        //assert
        assert(expected == driverViewModel.driverState.value.isSorted)
    }

    @Test
    fun test_reset_isSorted() {
        //arrange
        val expected = false
        //act
        driverViewModel.onDriverUserEvent(DriverUserEvent.ResetIsSorted)
        //assert
        assert(expected == driverViewModel.driverState.value.isSorted)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_get_drivers() = runTest {
        //arrange
//        val testScheduler = TestCoroutineScheduler()
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val expected = TestDriverRepositoryImpl.defaultDriverResponse()

        try {
            //act
            driverViewModel.onDriverUserEvent(event = DriverUserEvent.GetDrivers)

            //assert
            assert(expected.drivers == driverViewModel.driverState.value.drivers)
            assert(expected.routes == driverViewModel.driverState.value.routes)
        } finally {
            Dispatchers.resetMain()
        }
    }



    @Test
    fun test_select_driver() {
        //arrange
        val expected = TestDriverRepositoryImpl.defaultDriver()
        //act
        driverViewModel.onDriverUserEvent(DriverUserEvent.SelectDriver(TestDriverRepositoryImpl.defaultDriver()))
        //assert
        assert(expected == driverViewModel.driverState.value.selectedDriver)
    }

    @Test
    fun test_print_driver() {
        //arrange
        val expected = true
        //act
        driverViewModel.onDriverUserEvent(DriverUserEvent.PrintDrivers)
        //assert
        assert(expected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_delete_drivers_routes() = runTest {
        //arrange
//        val testScheduler = TestCoroutineScheduler()
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val expectedDriver = listOf<Driver>()
        val expectedRoute = listOf<Route>()

        try{
            //act
            driverViewModel.onDriverUserEvent(DriverUserEvent.DeleteDriversRoutes)

            //assert
            assert(expectedDriver == driverViewModel.driverState.value.drivers)
            assert(expectedRoute == driverViewModel.driverState.value.routes)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun test_set_error() {
        //arrange
        val expected = "Some Error Text"
        //act
        driverViewModel.onDriverUserEvent(DriverUserEvent.SetError(error = expected))
        //assert
        assert(expected == driverViewModel.driverState.value.errors)
    }

    @Test
    fun test_clear_error() {
        //arrange
        val expected = ""
        driverViewModel.onDriverUserEvent(DriverUserEvent.SetError(error = "Some error text"))
        //act
        driverViewModel.onDriverUserEvent(DriverUserEvent.ClearError)
        //assert
        assert(expected == driverViewModel.driverState.value.errors)
    }

}