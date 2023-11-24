package com.huhn.androidarchitectureexample.ui

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.huhn.androidarchitectureexample.R
import com.huhn.androidarchitectureexample.TestDriverDataImpl
import com.huhn.androidarchitectureexample.TestDriverRepositoryImpl
import com.huhn.androidarchitectureexample.viewmodel.DriverUserEvent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestRouteComposeUI {

    private lateinit var appContext : Context

    private val testDriverViewModel: TestDriverViewModel = TestDriverViewModel(TestDriverRepositoryImpl())

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        testDriverViewModel.onDriverUserEvent(DriverUserEvent.GetDrivers)
        testDriverViewModel.onDriverUserEvent(DriverUserEvent.SelectDriver(TestDriverDataImpl.defaultDriver()))


        // Render the Compose UI
        composeTestRule.setContent {
            // Call the entry point composable function
            RouteRoute(
                screenTitle = R.string.test_route_title,
                onBack = {
                    /* indicate navigation in Error message*/
                    testDriverViewModel.onDriverUserEvent(DriverUserEvent.SetError("Navigated to Driver Screen"))
                },
                driveViewModel = testDriverViewModel,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun testRouteScreenDisplay() {

        composeTestRule.onRoot().printToLog("TAG")

        val screen_title = appContext.resources.getString(R.string.test_route_title)
        composeTestRule.onNodeWithTag("screen_title")
            .assertIsDisplayed()
            .assertTextContains(screen_title)

        composeTestRule.onNodeWithTag("driver_errors").assertDoesNotExist()

        val selected_driver = appContext.resources.getString(R.string.route_picked)
        composeTestRule.onNodeWithTag("selected_driver")
            .assertIsDisplayed()
            .assertTextContains(selected_driver)

        composeTestRule.onNodeWithTag("driver_row")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("driver_id_select")
            .assertIsDisplayed()
            .assertTextEquals("1")
        composeTestRule.onNodeWithTag("driver_name_select")
            .assertIsDisplayed()
            .assertTextEquals(TestDriverDataImpl.defaultDriverName)

        val route_list_label = appContext.resources.getString(R.string.route_list_label)
        composeTestRule.onNodeWithTag("route_list_label")
            .assertIsDisplayed()
            .assertTextContains(route_list_label)

        composeTestRule.onAllNodesWithTag("route_row").assertCountEquals(7)
        composeTestRule.onAllNodesWithTag("route_id")
            .assertCountEquals(7)
//            .assertAll(isSelectable())
        composeTestRule.onAllNodesWithTag("route_name")
            .assertCountEquals(7)
//            .assertAll(isSelectable())

        val navigate_button = appContext.resources.getString(R.string.route_navigate_button)
        composeTestRule.onNodeWithTag("navigate_button")
            .assertIsDisplayed()
            .assertTextEquals(navigate_button)
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("route_errors")
            .assertIsDisplayed()
            .assertTextEquals("Navigated to Driver Screen")
    }
}