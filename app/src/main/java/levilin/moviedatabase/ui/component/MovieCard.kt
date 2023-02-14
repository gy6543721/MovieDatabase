package levilin.moviedatabase.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import levilin.moviedatabase.model.remote.list.MovieResult
import levilin.moviedatabase.ui.theme.favouriteButtonColor
import levilin.moviedatabase.ui.theme.screenBackgroundColor
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.viewmodel.SharedViewModel


@Composable
fun MovieCard(modifier: Modifier = Modifier, entry: MovieResult, viewModel: SharedViewModel = hiltViewModel(), navController: NavController) {

    Box(
        contentAlignment = Alignment.TopCenter,
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
        Box(contentAlignment = Alignment.TopEnd) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(15.dp))

                AsyncImage(
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
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            FavoriteButton(entry = entry, viewModel = viewModel)
        }
    }
}

@Composable
fun FavoriteButton(modifier: Modifier = Modifier, entry: MovieResult, viewModel: SharedViewModel) {

    val favoriteList by remember { mutableStateOf(viewModel.favoriteList) }
    var isFavorite by remember { mutableStateOf(false) }
    isFavorite = favoriteList.value.contains(entry.id)
    val isStatusChanged by remember { derivedStateOf { isFavorite } }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            viewModel.favoriteAction(isFavorite = isFavorite, entry = entry)
        }
    ) {
        Icon(
            tint = if (isStatusChanged) {
                Color.Red
            } else {
                MaterialTheme.colors.favouriteButtonColor
            },
            modifier = modifier.graphicsLayer {
                scaleX = 1.1f
                scaleY = 1.1f
            },
            imageVector = if (isStatusChanged) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = "Favorite Icon"
        )
    }
}