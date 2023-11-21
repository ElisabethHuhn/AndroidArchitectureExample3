package com.huhn.androidarchitectureexample.ui

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huhn.androidarchitectureexample.R
import com.huhn.androidarchitectureexample.viewmodel.DriverState
import com.huhn.androidarchitectureexample.viewmodel.DriverUserEvent
import com.huhn.androidarchitectureexample.viewmodel.DriverViewModel


@Composable
fun RouteRoute(
    screenTitle: Int,
    onBack: () -> Unit,
    driveViewModel: DriverViewModel,
){
    val driverState by driveViewModel.driverState.collectAsStateWithLifecycle()

    RouteScreen(
        screenTitle = screenTitle,
        driverState = driverState,
        onUserEvent = driveViewModel::onDriverUserEvent,
        onBack = onBack,
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    screenTitle: Int,
    driverState: DriverState,
    onUserEvent: (DriverUserEvent) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(screenTitle),
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            modifier = Modifier
                                .testTag(tag = "screen_title")
                                .align(Alignment.Center)
                        )
                    }
                },
            )
        }
    ) { it

        LazyColumn(
            modifier = Modifier
                .testTag(tag = "route_screen_column")
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(95.0.dp))
                if (driverState.errors.isNotEmpty()) {
                    Text(
                        text = driverState.errors,
                        fontSize = 20.sp,
                        modifier = Modifier.testTag(tag = "route_errors"),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        modifier = Modifier.testTag(tag = "clear_error_button"),
                        onClick = {
                            onUserEvent(DriverUserEvent.ClearError)
                        })
                    {
                        Text(text = stringResource(id = R.string.clear_error_button))
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(5.0.dp))
                Text(
                    modifier = Modifier.testTag(tag = "selected_driver"),
                    text = stringResource(R.string.route_picked),
                    fontSize = 20.sp,
                )
            }
            item {

                Spacer(modifier = Modifier.height(5.0.dp))
                Row (
                    modifier = Modifier.testTag(tag = "driver_row"),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.testTag(tag = "driver_id_select"),
                        text = driverState.selectedDriver?.id ?: ""
                    )
                    Text(
                        modifier = Modifier.testTag(tag = "driver_name_select"),
                        text = driverState.selectedDriver?.name ?: ""
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(5.0.dp))
                UnderlinedText(
                    modifier = Modifier.testTag(tag = "list_label"),
                    textString = stringResource(R.string.route_list)
                )

                Spacer(modifier = Modifier.height(15.0.dp))
            }
            driverState.routes?.let {routes ->
                val numberOfRoutes= routes.size
                items(numberOfRoutes) { position ->
                    val route = routes[position]
                    Row (
                        modifier = Modifier.testTag(tag = "route_row"),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.testTag(tag = "route_id"),
                            text = route.id.toString()
                        )
                        Text(
                            modifier = Modifier.testTag(tag = "route_name"),
                            text = " - ${route.name}"
                        )
                        Text(
                            modifier = Modifier.testTag(tag = "route_type"),
                            text = " - ${route.type}"
                        )
                    }
                }
            }

            item {
                Button(
                    modifier = Modifier.testTag(tag = "navigate_button"),
                    onClick = {
                        //navigate to Driver Screen
                        onBack.invoke()
                    })
                {
                    Text(text = stringResource(id = R.string.route_button))
                }
            }
        }
    }
}
