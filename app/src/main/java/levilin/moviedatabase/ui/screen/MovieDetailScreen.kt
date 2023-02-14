package levilin.moviedatabase.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import levilin.moviedatabase.ui.theme.screenBackgroundColor
import levilin.moviedatabase.ui.theme.screenTextColor
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.viewmodel.SharedViewModel

@Composable
fun MovieDetailScreen(navController: NavController, viewModel: SharedViewModel, id: String) {

    val movieDetail by remember { viewModel.movieDetail }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.screenBackgroundColor)
        .padding(bottom = 16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = id, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(10.dp))
            Text(text = movieDetail.title, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(10.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(ConstantValue.IMAGE_BASE_URL + movieDetail.posterPath)
                    .crossfade(true)
                    .build(),
                contentDescription = movieDetail.title,
                modifier = Modifier.size(200.dp),
                alignment = Alignment.Center
            )
            Text(text = movieDetail.overview, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(10.dp))
        }
    }


}