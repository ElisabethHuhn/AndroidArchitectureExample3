package com.huhn.androidarchitectureexample.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.huhn.androidarchitectureexample.BuildConfig
import com.huhn.androidarchitectureexample.R
import com.huhn.androidarchitectureexample.TestDriverRepositoryImpl
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestComposeUI {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDriveScreenDisplay() {
        // Set up the test environment
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext


        // Render the Compose UI
        composeTestRule.setContent {
            // Call the entry point composable function
            DriverRoute(
                screenTitle = R.string.test_driver_title,
                onNavigateToRoute = {
                    /* Somehow indicate navigation */
                },
                driveViewModel = TestDriverViewModel(TestDriverRepositoryImpl()),
            )
        }
        composeTestRule.waitForIdle()

        composeTestRule.onRoot().printToLog("TAG")

        val screen_title = appContext.resources.getString(R.string.test_driver_title)
        composeTestRule.onNodeWithTag("screen_title")
            .assertIsDisplayed()
            .assertTextContains(screen_title)

        composeTestRule.onNodeWithTag("sort_button").assertIsDisplayed()

        composeTestRule.onNodeWithTag("driver_screen_column").assertIsDisplayed()

        composeTestRule.onNodeWithTag("driver_errors").assertDoesNotExist()

        composeTestRule.onNodeWithTag("clear_error_button").assertDoesNotExist()

        val build_config_type = "Build Config Type: ${BuildConfig.BUILD_TYPE_STRING}"
        composeTestRule.onNodeWithTag("build_config_type")
            .assertIsDisplayed()
            .assertTextContains(build_config_type)

        val build_resource_label = appContext.resources.getString(R.string.build_resource_label)
        composeTestRule.onNodeWithTag("build_resource_label")
            .assertIsDisplayed()
            .assertTextContains(build_resource_label)
        val build_config_string_resource = appContext.resources.getString(R.string.build_type_res)
        composeTestRule.onNodeWithTag("build_config_string_resource")
            .assertIsDisplayed()
            .assertTextContains(build_config_string_resource)

        composeTestRule.onNodeWithTag("select_driver").assertIsDisplayed()

        composeTestRule.onNodeWithTag("no_drivers").assertDoesNotExist()

        //at least one driver is displayed
        composeTestRule.onAllNodesWithTag("driver_row").assertCountEquals(6)
        composeTestRule.onAllNodesWithTag("driver_id_select")
            .assertCountEquals(6)
//            .assertAll(isSelectable())
        composeTestRule.onAllNodesWithTag("driver_name_select")
            .assertCountEquals(6)
//            .assertAll(isSelectable())

        val sorted_indicator = appContext.resources.getString(R.string.sorted_indicator, "false")
        composeTestRule.onNodeWithTag("sorted_indicator")
            .assertIsDisplayed()
            .assertTextEquals(sorted_indicator)

        val print_button = appContext.resources.getString(R.string.print_button)
        composeTestRule.onNodeWithTag("print_button")
            .assertIsDisplayed()
            .assertTextEquals(print_button)

        val delete_button = appContext.resources.getString(R.string.delete_button)
        composeTestRule.onNodeWithTag("delete_button")
            .assertIsDisplayed()
            .assertTextEquals(delete_button)

        val reload_button = appContext.resources.getString(R.string.reload_button)
        composeTestRule.onNodeWithTag("reload_button")
            .assertIsDisplayed()
            .assertTextContains(reload_button)
    }
    fun testDriveScreenBehavior() {
        // Set up the test environment
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext


        // Render the Compose UI
        composeTestRule.setContent {
            // Call the entry point composable function
            DriverRoute(
                screenTitle = R.string.test_driver_title,
                onNavigateToRoute = {
                    /* Somehow indicate navigation */
                },
                driveViewModel = TestDriverViewModel(TestDriverRepositoryImpl()),
            )
        }

//        // Interact with the UI and trigger user actions
//        composeTestRule.onNodeWithText("Force remote fetch of Drivers and Routes").performClick()
//
//        // Assert the expected UI states or behaviors
//        composeTestRule.onNodeWithText("Clicked!").assertIsDisplayed()
    }


}