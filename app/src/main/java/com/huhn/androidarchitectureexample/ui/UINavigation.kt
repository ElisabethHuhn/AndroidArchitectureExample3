package com.huhn.androidarchitectureexample.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huhn.androidarchitectureexample.R
import com.huhn.androidarchitectureexample.viewmodel.DriverViewModel
import org.koin.androidx.compose.koinViewModel

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
}

/*
 * The NavHost is single source of truth for all screen navigation in the app
 */
@ExperimentalMaterial3Api
@Composable
fun MainNavGraph(
    driverViewModel: DriverViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination =  DriverDestination.route
    ){
        composable(DriverDestination.route){
            //pass navigation as parameter
            DriverRoute(
                screenTitle = DriverDestination.title,
                onNavigateToRoute = navController::navigateToRouteRoute,
                driveViewModel = driverViewModel,
            )
        }

        composable(
            route = RouteDestination.route,
        ){ backStackEntry ->
            //pass navigation as parameter
            RouteRoute(
                screenTitle = RouteDestination.title,
                onBack = navController::navigateToDriverRoute, //TODO try with backStackEntry
                driveViewModel = driverViewModel,
            )
        }
    }
}

fun NavController.navigateToDriverRoute(navOptions: NavOptions? = null){
    this.navigate(route = DriverDestination.route, navOptions = navOptions)
}
fun NavController.navigateToRouteRoute(navOptions: NavOptions? = null){
    this.navigate(route = RouteDestination.route, navOptions = navOptions)
}

