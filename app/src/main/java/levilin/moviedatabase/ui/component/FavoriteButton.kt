package levilin.moviedatabase.ui.component

import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import levilin.moviedatabase.model.list.MovieResult
import levilin.moviedatabase.ui.theme.favouriteButtonColor
import levilin.moviedatabase.viewmodel.SharedViewModel

@Composable
fun FavoriteButton(modifier: Modifier = Modifier, entry: MovieResult, viewModel: SharedViewModel) {
    val favoriteList by remember { mutableStateOf(viewModel.favoriteList) }
    var isFavorite by remember { mutableStateOf(false) }
    isFavorite = favoriteList.value.contains(entry)
    val isStatusChanged by remember { derivedStateOf { isFavorite } }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            viewModel.favoriteAction(isFavorite = isFavorite, entry = entry)
            viewModel.loadFavoriteList()
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