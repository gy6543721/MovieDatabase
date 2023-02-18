package levilin.moviedatabase.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import levilin.moviedatabase.viewmodel.SharedViewModel

@Composable
fun MovieList(navController: NavController, viewModel: SharedViewModel = hiltViewModel()) {
    val moviesList by remember { mutableStateOf(value = viewModel.movieList) }
    val loadingErrorMessage by remember { mutableStateOf(value = viewModel.errorMovieListMessage) }
    val isLoading by remember { mutableStateOf(value = viewModel.isMovieListLoading) }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if(moviesList.value.size % 2 == 0) {
            moviesList.value.size / 2
        } else {
            moviesList.value.size / 2 + 1
        }
        items(itemCount) { rowIndex ->
            if(rowIndex >= itemCount - 1) {
                LaunchedEffect(key1 = true) {
                    viewModel.loadMovieList()
                }
            }
            ListRow(rowIndex = rowIndex, entries = moviesList.value, navController = navController, viewModel = viewModel)
        }
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(10.dp)) {
        if(isLoading.value && loadingErrorMessage.value.isBlank()) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(loadingErrorMessage.value.isNotBlank()) {
            RetrySection(error = loadingErrorMessage.value, onRetry = {
                viewModel.loadMovieList()
            })
        }
    }
}