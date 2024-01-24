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
import com.huhn.androidarchitectureexample.viewmodel.DatesViewModel
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
object DatesDestination : ScreenDestination {
    override val route: String
        get() = "dates_screen"
    override val title: Int
        get() = R.string.dates_title
}
object EnhancedDateDestination : ScreenDestination {
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
    datesViewModel: DatesViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination =  DatesDestination.route
    ){
        composable(DatesDestination.route){
            //pass navigation as parameter
            DatesRoute(
                screenTitle = DatesDestination.title,
                onNavigateToEnhancedDates = navController::navigateToEnhancedDateEnhancedDate,
                datesViewModel = datesViewModel,
            )
        }

//        composable(
//            route = EnhancedDateDestination.route,
//        ){ backStackEntry ->
//            //pass navigation as parameter
//            EnhancedDateRoute(
//                screenTitle = EnhancedDateDestination.title,
//                onBack = navController::navigateToDatesEnhancedDate, //TODO try with backStackEntry
//                driveViewModel = datesViewModel,
//            )
//        }
    }
}

fun NavController.navigateToDatesEnhancedDate(navOptions: NavOptions? = null){
    this.navigate(route = DatesDestination.route, navOptions = navOptions)
}
fun NavController.navigateToEnhancedDateEnhancedDate(navOptions: NavOptions? = null){
    this.navigate(route = EnhancedDateDestination.route, navOptions = navOptions)
}

