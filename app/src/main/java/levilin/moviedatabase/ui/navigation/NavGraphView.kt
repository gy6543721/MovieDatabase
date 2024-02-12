package levilin.moviedatabase.ui.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import levilin.moviedatabase.ui.screen.FavoriteListScreen
import levilin.moviedatabase.ui.screen.MovieDetailScreen
import levilin.moviedatabase.ui.screen.MovieListScreen
import levilin.moviedatabase.viewmodel.SharedViewModel

@ExperimentalComposeUiApi
@Composable
fun NavGraphView(navController: NavHostController, sharedViewModel: SharedViewModel, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = "movie_list_screen",
        modifier = modifier.systemBarsPadding()
    ) {
        composable("movie_list_screen") {
            MovieListScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable("favorite_list_screen") {
            FavoriteListScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(
            "movie_detail_screen/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) {
            MovieDetailScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

    }
}
