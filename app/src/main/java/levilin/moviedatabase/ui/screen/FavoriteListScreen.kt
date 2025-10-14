package levilin.moviedatabase.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import levilin.moviedatabase.ui.component.FavoriteList
import levilin.moviedatabase.viewmodel.MovieDatabaseViewModel

@Composable
fun FavoriteListScreen(navController: NavController, viewModel: MovieDatabaseViewModel = hiltViewModel()) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            FavoriteList(navController = navController, viewModel = viewModel)
        }
    }
}