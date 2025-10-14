package levilin.moviedatabase.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import levilin.moviedatabase.model.list.MovieResult
import levilin.moviedatabase.viewmodel.MovieDatabaseViewModel

@Composable
fun ListRow(rowIndex: Int, entries: List<MovieResult>, navController: NavController, viewModel: MovieDatabaseViewModel = hiltViewModel()) {
    Column {
        Row {
            MovieCard(
                entry = entries[rowIndex * 2],
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2) {
                MovieCard(
                    entry = entries[rowIndex * 2 + 1],
                    viewModel = viewModel,
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
