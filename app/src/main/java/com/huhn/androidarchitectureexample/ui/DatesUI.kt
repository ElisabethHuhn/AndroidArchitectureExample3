package com.huhn.androidarchitectureexample.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huhn.androidarchitectureexample.R
import com.huhn.androidarchitectureexample.viewmodel.DatesState
import com.huhn.androidarchitectureexample.viewmodel.DatesUserEvent
import com.huhn.androidarchitectureexample.viewmodel.DatesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatesRoute(
    screenTitle: Int,
    onNavigateToEnhancedDates: () -> Unit,
    datesViewModel: DatesViewModel,
){
    val DatesState by datesViewModel.datesState.collectAsStateWithLifecycle()

    DatesScreen(
        screenTitle = screenTitle,
        datesState = DatesState,
        onUserEvent = datesViewModel::onDatesUserEvent,
        onNavigateToEnhancedDates = onNavigateToEnhancedDates,
    )
}

@ExperimentalMaterial3Api
@Composable
fun DatesScreen(
    screenTitle : Int,
    datesState: DatesState,
    onUserEvent: (DatesUserEvent) -> Unit,
    onNavigateToEnhancedDates: () -> Unit,
) {
    //actually make the API call to get the list of Datess
    val firstTimeFlag = remember { mutableStateOf(true) }
    if (firstTimeFlag.value) {
        firstTimeFlag.value = false
        onUserEvent(DatesUserEvent.GetDates)
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

    ) { it
        LazyColumn(
            modifier = Modifier
                .testTag(tag = "Dates_screen_column")
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(95.0.dp))
            }
            /// errors is not empty



//            item {
//                Button(
//                    modifier = Modifier.testTag(tag = "delete_button"),
//                    onClick = {
//                        //Delete All Datess and Routes
//                        onUserEvent(DatesUserEvent.DeleteDatessRoutes)
//
//                        //reset is sorted
//                        onUserEvent(DatesUserEvent.ResetIsSorted)
//                    })
//                {
//                    Text(text = stringResource(id = R.string.delete_button))
//                }
//            }
//            item {
//                Button(
//                    modifier = Modifier.testTag(tag = "reload_button"),
//                    onClick = {
//                        //Delete All Datess and Routes
//                        onUserEvent(DatesUserEvent.DeleteDatessRoutes)
//
//                        //reset is sorted
//                        onUserEvent(DatesUserEvent.ResetIsSorted)
//
//                        //read in new Dates list
//                        onUserEvent(DatesUserEvent.GetDatess)
//                    })
//                {
//                    Text(text = stringResource(id = R.string.reload_button))
//                }
//            }


            item {
                Spacer(modifier = Modifier.height(15.0.dp))
                Text(
                    modifier = Modifier.testTag(tag = "select_Dates_label"),
                    text = stringResource(R.string.select_dates),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(15.0.dp))

            }
            item {
                if (datesState.dates.isNullOrEmpty()) {
                    Text(
                        text = stringResource(R.string.no_dates),
                        fontSize = 20.sp,
                        modifier = Modifier.testTag(tag = "no_dates"),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
            item {
                if (datesState.errors.isNotEmpty()) {
                    Text(
                        text = datesState.errors,
                        fontSize = 20.sp,
                        modifier = Modifier.testTag(tag = "dates_errors"),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        modifier = Modifier.testTag(tag = "clear_error_button"),
                        onClick = {
                            onUserEvent(DatesUserEvent.ClearError)
                            onUserEvent(DatesUserEvent.DeleteDates)
                        })
                    {
                        Text(text = stringResource(id = R.string.clear_error_button))
                    }
                }
            }
            datesState.dates?.let { datesList ->
                items(datesList.size) { position ->
                    val dates = datesState.dates[position]
                    Text(
                        modifier = Modifier
                            .testTag(tag = "Dates_id_select")
                            .clickable {
                                onUserEvent(DatesUserEvent.SelectDate(dates))
                                onNavigateToEnhancedDates.invoke()
                            },
                        text = dates.date
                    )
                }

            }
        }
    }
}
