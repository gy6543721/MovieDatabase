package levilin.moviedatabase.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import levilin.moviedatabase.viewmodel.SharedViewModel

@Composable
fun FavoriteList(navController: NavController, viewModel: SharedViewModel = hiltViewModel()) {
    val favoriteList by remember { viewModel.favoriteList }
    val loadingError by remember { viewModel.loadingError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if(favoriteList.size % 2 == 0) {
            favoriteList.size / 2
        } else {
            favoriteList.size / 2 + 1
        }
        items(itemCount) { rowIndex ->
            if(rowIndex >= itemCount - 1) {
                LaunchedEffect(key1 = true) {
                    viewModel.loadMoviesList()
                }
            }
            ListRow(rowIndex = rowIndex, entries = favoriteList, navController = navController, viewModel = viewModel)
        }
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if(isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(loadingError.isNotEmpty()) {
            RetrySection(error = loadingError) {
                viewModel.loadMoviesList()
            }
        }
    }
}