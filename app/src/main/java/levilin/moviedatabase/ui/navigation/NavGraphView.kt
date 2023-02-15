package levilin.moviedatabase.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import levilin.moviedatabase.ui.screen.FavoriteListScreen
import levilin.moviedatabase.ui.screen.MovieDetailScreen
import levilin.moviedatabase.ui.screen.MovieListScreen
import levilin.moviedatabase.viewmodel.SharedViewModel

@Composable
fun NavGraphView(navController: NavHostController, sharedViewModel: SharedViewModel) {
    NavHost(
        navController = navController,
        startDestination = "movie_list_screen"
    ) {
        composable("movie_list_screen") {
            MovieListScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable("favorite_list_screen") {
            FavoriteListScreen()
        }
        composable(
            "movie_detail_screen/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val id = remember {
                navBackStackEntry.arguments!!.getString("id")!!
            }
            MovieDetailScreen(
                navController = navController,
                viewModel = sharedViewModel,
                id = id
            )
        }

    }
}
