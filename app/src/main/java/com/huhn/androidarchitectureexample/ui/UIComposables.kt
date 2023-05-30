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
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.huhn.androidarchitectureexample.BuildConfig
import com.huhn.androidarchitectureexample.R
import com.huhn.androidarchitectureexample.viewmodel.DriverViewModelImpl

/*
 * A ScreenDestination keeps together all the information needed
 * for navigation to/from the screen
 * Every screen has one of these ScreenDestinations defined for it
 * Best reference https://bignerdranch.com/blog/using-the-navigation-component-in-jetpack-compose/
 */
interface ScreenDestination {
    val route: String
    val title: Int
}
object DriverDestination : ScreenDestination {
    override val route: String
        get() = "driver_screen"
    override val title: Int
        get() = R.string.driver_title
}
object RouteDestination : ScreenDestination {
    override val route: String
        get() = "route_screen"
    override val title: Int
        get() = R.string.route_title
    const val driverIdArg = "driverId"
    val routeWithArg: String = "$route/{$driverIdArg}"
    val arguments = listOf(navArgument(driverIdArg) {type = NavType.StringType})
    fun getNavigationDriverToRoute(driverId: String) = "$route/$driverId"
}

/*
 * The NavHost is single source of truth for all screen navigation in the app
 */
@ExperimentalMaterial3Api
@Composable
fun MainNavGraph(
    driverViewModel: DriverViewModelImpl,
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination =  DriverDestination.route
    ){
        composable(DriverDestination.route){
            //pass navigation as parameter
            DriverScreen(
                screenTitle = DriverDestination.title,
                onDriverSelect = {
                    val toDes = RouteDestination.getNavigationDriverToRoute(it)
                    navController.navigate(
                        toDes
                    )
                },
                viewModel = driverViewModel,
            )
        }

        composable(
            route = RouteDestination.routeWithArg,
            arguments = RouteDestination.arguments,
        ){ backStackEntry ->
            //pass navigation as parameter
            RouteScreen(
                screenTitle = RouteDestination.title,
                driverId = backStackEntry.arguments?.getString(
                    RouteDestination.driverIdArg
                ) ?: "",
                onBack = {
                    val toDest = DriverDestination.route
                     navController.navigate(toDest)
                },
                viewModel = driverViewModel,
            )
        }
    }
}



@ExperimentalMaterial3Api
@Composable
fun DriverScreen(
    screenTitle : Int,
    onDriverSelect: (driverId: String) -> Unit,
    viewModel: DriverViewModelImpl

) {
    val forceRecomposeState = remember { mutableStateOf( ForceRecomposeState.RECOMPOSE_A) }
    val drivers = viewModel._drivers.observeAsState()

    //actually make the API call
    viewModel.setDrivers()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(screenTitle),
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                content = { Text(text = "Sort")},
                onClick = {
                    /*TODO Sort alphabetically by last name*/
                    var sortingParm = true
                    if (viewModel.isSorted) sortingParm = false
                    viewModel.isSorted = sortingParm
                    viewModel.setDrivers()

                    /* TODO redisplay DriverScreen */
//                    currentComposer.composition.recompose()
                    if (forceRecomposeState.value == ForceRecomposeState.RECOMPOSE_A)
                        forceRecomposeState.value = ForceRecomposeState.RECOMPOSE_B
                    else
                        forceRecomposeState.value = ForceRecomposeState.RECOMPOSE_A
                },
                shape = RectangleShape,
            )
        }

    ) { it


        Spacer(modifier = Modifier.height(95.0.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(95.0.dp))
                Text(text = BuildConfig.BUILD_TYPE_STRING)
            }
            item {
                Spacer(modifier = Modifier.height(5.0.dp))
                Text(text = stringResource(id = R.string.build_type_res))
            }

            item {
                Spacer(modifier = Modifier.height(15.0.dp))
                UnderlinedText(textString = stringResource(id = R.string.driver_list))
            }

            drivers.value?.size?.let { numberOfDrivers ->
                items(numberOfDrivers) { position ->
                    val driver = drivers.value!![position]
                    Row (
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.clickable { onDriverSelect(driver.id)  },
                            text = driver.id
                        )
                        Text(
                            modifier = Modifier.clickable { onDriverSelect(driver.id)  },
                            text = driver.name
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(15.0.dp))
                Text(
                    text = stringResource(R.string.nav_instructions),
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontStyle = FontStyle.Italic
                )
            }
            item {
                Spacer(modifier = Modifier.height(15.0.dp))
                Text(
                    text = stringResource(R.string.is_sorted, viewModel.isSorted),
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Button(
                    onClick = {
                        //Print out the list of drivers
                        viewModel.printDrivers()
                    })
                {
                    Text(text = stringResource(id = R.string.print_button))
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    screenTitle: Int,
    driverId: String,
    onBack: () -> Unit,
    viewModel: DriverViewModelImpl
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(screenTitle),
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
            )
        }
    ) { it
        val routes = viewModel._routes.observeAsState()

        LazyColumn(
            modifier = Modifier.fillMaxWidth(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(95.0.dp))
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.route_picked),
                    fontSize = 20.sp,
                )
            }
            item {
                val driver = viewModel.findDriver(driverId)
                Spacer(modifier = Modifier.height(5.0.dp))
                Row (
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Note defaults if driver is null
                    Text(text = driver?.id ?: "0")
                    Text(text = driver?.name ?: "Driver Zero")
                }
            }
            item {
                Spacer(modifier = Modifier.height(5.0.dp))
                UnderlinedText(textString = stringResource(R.string.route_list))

                Spacer(modifier = Modifier.height(15.0.dp))
            }
            routes.value?.size?.let { numberOfRoutes ->
                items(numberOfRoutes) { position ->
                    val route = routes.value!![position]
                    Row (
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = route.id.toString()
                        )
                        Text(
                            text = route.name
                        )
                    }
                }
            }
            item {
                Button(
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

@Composable
fun UnderlinedText(textString: String) {
    Text(
        text = textString,
        textDecoration = TextDecoration.Underline,
        fontSize = 20.sp,
    )
}