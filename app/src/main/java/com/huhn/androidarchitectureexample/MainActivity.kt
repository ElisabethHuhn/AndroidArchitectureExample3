package com.huhn.androidarchitectureexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huhn.androidarchitectureexample.ui.DriverDestination
import com.huhn.androidarchitectureexample.ui.DriverScreen
import com.huhn.androidarchitectureexample.ui.MainNavGraph
import com.huhn.androidarchitectureexample.ui.RouteDestination
import com.huhn.androidarchitectureexample.ui.RouteScreen
import com.huhn.androidarchitectureexample.ui.theme.AndroidArchitectureExampleTheme
import com.huhn.androidarchitectureexample.viewmodel.DriverViewModel

class MainActivity : ComponentActivity() {
    private val driveViewModel: DriverViewModel by viewModels()

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidArchitectureExampleTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
////                    Greeting("Android")
//                }

                MainNavGraph( driverViewModel = driveViewModel )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidArchitectureExampleTheme {
        Greeting("Android")
    }
}