package levilin.moviedatabase.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import levilin.moviedatabase.ui.navigation.BottomNavView
import levilin.moviedatabase.ui.navigation.NavGraphView
import levilin.moviedatabase.ui.theme.MovieDatabaseTheme
import levilin.moviedatabase.viewmodel.SharedViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDatabaseTheme {
                val navController = rememberNavController()

                Scaffold(bottomBar = { BottomNavView(navController = navController) }) { innerPadding ->
                    NavGraphView(navController = navController, sharedViewModel = sharedViewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}