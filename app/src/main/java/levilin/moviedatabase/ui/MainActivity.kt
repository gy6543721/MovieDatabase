package levilin.moviedatabase.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import levilin.moviedatabase.ui.screen.MovieListScreen
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
                NavHost(
                    navController = navController,
                    startDestination = "movie_list_screen"
                ) {
                    composable("movie_list_screen") {
                        MovieListScreen(navController = navController, viewModel = sharedViewModel)
                    }
                }
            }
        }
    }
}