package levilin.moviedatabase.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import levilin.moviedatabase.model.list.MovieResult
import levilin.moviedatabase.ui.theme.screenBackgroundColor
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.viewmodel.MovieDatabaseViewModel

@Composable
fun MovieCard(modifier: Modifier = Modifier, entry: MovieResult, viewModel: MovieDatabaseViewModel = hiltViewModel(), navController: NavController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .aspectRatio(1f)
            .background(MaterialTheme.colors.screenBackgroundColor)
            .clickable {
                viewModel.loadMovieDetail(id = "${entry.id}")
                navController.navigate(
                    "movie_detail_screen/${entry.id}"
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            LoadableAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(ConstantValue.IMAGE_BASE_URL + entry.posterPath)
                    .crossfade(true)
                    .build(),
                contentDescription = entry.title,
                modifier = Modifier.size(120.dp),
                alignment = Alignment.Center
            )
            Text(
                text = entry.title,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box(
            modifier = Modifier.align(Alignment.TopEnd) // Align this box to the top end
        ) {
            FavoriteButton(entry = entry, viewModel = viewModel)
        }
    }
}