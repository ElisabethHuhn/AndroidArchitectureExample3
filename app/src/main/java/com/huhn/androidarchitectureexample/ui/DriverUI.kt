package com.huhn.androidarchitectureexample.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huhn.androidarchitectureexample.BuildConfig
import com.huhn.androidarchitectureexample.R
import com.huhn.androidarchitectureexample.viewmodel.DriverState
import com.huhn.androidarchitectureexample.viewmodel.DriverUserEvent
import com.huhn.androidarchitectureexample.viewmodel.DriverViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverRoute(
    screenTitle: Int,
    onNavigateToRoute: () -> Unit,
    driveViewModel: DriverViewModel,
){
    val driverState by driveViewModel.driverState.collectAsStateWithLifecycle()

    DriverScreen(
        screenTitle = screenTitle,
        driverState = driverState,
        onUserEvent = driveViewModel::onDriverUserEvent,
        onNavigateToRoute = onNavigateToRoute,
    )
}

@ExperimentalMaterial3Api
@Composable
fun DriverScreen(
    screenTitle : Int,
    driverState: DriverState,
    onUserEvent: (DriverUserEvent) -> Unit,
    onNavigateToRoute: () -> Unit,
) {
    //actually make the API call to get the list of drivers
    val firstTimeFlag = remember { mutableStateOf(true) }
    if (firstTimeFlag.value) {
        firstTimeFlag.value = false
        onUserEvent(DriverUserEvent.GetDrivers)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .testTag(tag = "screen_title")
                                .align(Alignment.Center),
                            text = stringResource(screenTitle),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag(tag = "sort_button"),
                content = { Text(text = stringResource(R.string.sort_button)) },
                onClick = {
                    onUserEvent(DriverUserEvent.ToggleIsSorted)
                    onUserEvent(DriverUserEvent.GetDrivers)
                },
                shape = RectangleShape,
            )
        }
    ) { it
        LazyColumn(
            modifier = Modifier
                .testTag(tag = "driver_screen_column")
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(95.0.dp))
            }
            /// errors is not empty

            item {
                Spacer(modifier = Modifier.height(5.0.dp))
                Text(
                    modifier = Modifier.testTag(tag = "build_config_type"),
                    text = "Build Config Type: ${BuildConfig.BUILD_TYPE_STRING}"
                )
            }
            item {
                Spacer(modifier = Modifier.height(5.0.dp))
                Text(
                    modifier = Modifier.testTag(tag = "build_resource_label"),
                    text = stringResource(R.string.build_resource_label)
                )
                Text(
                    modifier = Modifier.testTag(tag = "build_config_string_resource"),
                    text = stringResource(id = R.string.build_type_res)
                )
            }
            item {
                Spacer(modifier = Modifier.height(15.0.dp))
                Text(
                    modifier = Modifier.testTag(tag = "select_driver_label"),
                    text = stringResource(R.string.select_driver),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(15.0.dp))
            }
            item {
                if (driverState.drivers.isNullOrEmpty()) {
                        Text(
                        text = stringResource(R.string.no_drivers),
                        fontSize = 20.sp,
                        modifier = Modifier.testTag(tag = "no_drivers"),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
            /////drivers
            item {
                Spacer(modifier = Modifier.height(15.0.dp))
                Text(
                    text = stringResource(R.string.sorted_indicator, driverState.isSorted.toString()),
                    fontSize = 20.sp,
                    modifier = Modifier.testTag(tag = "sorted_indicator"),
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Button(
                    modifier = Modifier.testTag(tag = "print_button"),
                    onClick = {
                        //Print out the list of drivers
                        onUserEvent(DriverUserEvent.PrintDrivers)
                    })
                {
                    Text(text = stringResource(id = R.string.print_button))
                }
            }
            item {
                Button(
                    modifier = Modifier.testTag(tag = "delete_button"),
                    onClick = {
                        //Delete All Drivers and Routes
                        onUserEvent(DriverUserEvent.DeleteDriversRoutes)

                        //reset is sorted
                        onUserEvent(DriverUserEvent.ResetIsSorted)
                    })
                {
                    Text(text = stringResource(id = R.string.delete_button))
                }
            }
            item {
                Button(
                    modifier = Modifier.testTag(tag = "reload_button"),
                    onClick = {
                        //Delete All Drivers and Routes
                        onUserEvent(DriverUserEvent.DeleteDriversRoutes)

                        //reset is sorted
                        onUserEvent(DriverUserEvent.ResetIsSorted)

                        //read in new driver list
                        onUserEvent(DriverUserEvent.GetDrivers)
                    })
                {
                    Text(text = stringResource(id = R.string.reload_button))
                }
            }
            item {
                Spacer(modifier = Modifier.height(15.0.dp))
                Text(
                    text = stringResource(id = R.string.fab_label),
                    fontSize = 20.sp,
                    modifier = Modifier.testTag(tag = "fab_label"),
                    fontWeight = FontWeight.Bold
                )
            }

            if (driverState.errors.isNotEmpty()) {
                item {
                    Text(
                        text = driverState.errors,
                        fontSize = 20.sp,
                        modifier = Modifier.testTag(tag = "driver_errors"),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        modifier = Modifier.testTag(tag = "clear_error_button"),
                        onClick = {
                            onUserEvent(DriverUserEvent.ClearError)
                            onUserEvent(DriverUserEvent.DeleteDriversRoutes)
                        })
                    {
                        Text(text = stringResource(id = R.string.clear_error_button))
                    }
                }
            }
            driverState.drivers?.let {driverList ->
                items(driverList.size) { position ->
                    val driver = driverState.drivers[position]

                    Row(
                        modifier = Modifier.testTag(tag = "driver_row"),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .testTag(tag = "driver_id_select")
                                .clickable {
                                    onUserEvent(DriverUserEvent.SelectDriver(driver))
                                    onNavigateToRoute.invoke()
                                },
                            text = driver.id
                        )
                        Text(
                            modifier = Modifier
                                .testTag(tag = "driver_name_select")
                                .clickable {
                                    onUserEvent(DriverUserEvent.SelectDriver(driver = driver))
                                    onNavigateToRoute.invoke()
                                },
                            text = driver.name
                        )
                    }
                }
            }


        }
    }
}
