package com.huhn.androidarchitectureexample.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    fun DriveScreenTest() {
        // Set up the test environment

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

        composeTestRule.onRoot().printToLog("TAG")
        composeTestRule.onNodeWithText("Build Config Type: DEBUG_STRING").assertIsDisplayed()

//        // Interact with the UI and trigger user actions
//        composeTestRule.onNodeWithText("Force remote fetch of Drivers and Routes").performClick()
//
//        // Assert the expected UI states or behaviors
//        composeTestRule.onNodeWithText("Clicked!").assertIsDisplayed()
    }
}