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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val drivers = viewModel.driversFlow.collectAsStateWithLifecycle()
    val routes = viewModel.routesFlow.collectAsStateWithLifecycle()
    val isSortedFlag = viewModel.isSorted.observeAsState()

    //actually make the API call
    viewModel.getDrivers()

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

                    viewModel.toggleIsSorted()
                    viewModel.getDrivers()
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
                Text(text = "Build Config Type: ${BuildConfig.BUILD_TYPE_STRING}")
            }
            item {
                Spacer(modifier = Modifier.height(5.0.dp))
                Text(text = "Build Type String Resource =")
                Text(text = stringResource(id = R.string.build_type_res))
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

            drivers.value.size.let { numberOfDrivers ->
                items(numberOfDrivers) { position ->
                    val driver = drivers.value[position]
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
                    text = stringResource(id = R.string.is_sorted_label),
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.is_sorted, isSortedFlag.value.toString()),
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Button(
                    onClick = {
                        //Print out the list of drivers
                       viewModel.printDrivers(drivers.value)
                    })
                {
                    Text(text = stringResource(id = R.string.print_button))
                }
            }

            item {
                Button(
                    onClick = {
                        //Delete All Drivers
                        drivers.value.forEach { driver ->
                            viewModel.deleteDriver(driver)
                        }
                        //And all routes
                        routes.value.forEach { route ->
                            viewModel.deleteRoute(route)
                        }
                    })
                {
                    Text(text = stringResource(id = R.string.delete_button))
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
        val routes = viewModel.routesFlow.collectAsStateWithLifecycle()
        val driver = viewModel.foundDriverFlow.collectAsStateWithLifecycle()
        viewModel.findDriver(driverId)

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

                Spacer(modifier = Modifier.height(5.0.dp))
                Row (
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = driver.value.id)
                    Text(text = driver.value.name)
                }
            }
            item {
                Spacer(modifier = Modifier.height(5.0.dp))
                UnderlinedText(textString = stringResource(R.string.route_list))

                Spacer(modifier = Modifier.height(15.0.dp))
            }
            routes.value.size.let { numberOfRoutes ->
                items(numberOfRoutes) { position ->
                    val route = routes.value[position]
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