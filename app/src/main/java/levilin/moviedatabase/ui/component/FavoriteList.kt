package levilin.moviedatabase.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import levilin.moviedatabase.viewmodel.MovieDatabaseViewModel

@Composable
fun FavoriteList(navController: NavController, viewModel: MovieDatabaseViewModel = hiltViewModel()) {
    val favoriteList by remember { mutableStateOf(value = viewModel.favoriteList) }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if(favoriteList.value.size % 2 == 0) {
            favoriteList.value.size / 2
        } else {
            favoriteList.value.size / 2 + 1
        }
        items(itemCount) { rowIndex ->
            ListRow(rowIndex = rowIndex, entries = favoriteList.value, navController = navController, viewModel = viewModel)
        }
    }
}