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
import androidx.compose.ui.res.stringResource
import levilin.moviedatabase.model.list.MovieResult
import levilin.moviedatabase.ui.theme.favouriteButtonColor
import levilin.moviedatabase.viewmodel.MovieDatabaseViewModel
import levilin.moviedatabase.R

@Composable
fun FavoriteButton(modifier: Modifier = Modifier, entry: MovieResult, viewModel: MovieDatabaseViewModel) {
    var isFavorite by remember { mutableStateOf(false) }
    isFavorite = viewModel.checkFavorite(input = entry)

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            viewModel.favoriteAction(isFavorite = isFavorite, entry = entry)
            viewModel.loadFavoriteList()
        }
    ) {
        Icon(
            tint = if (isFavorite) {
                Color.Red
            } else {
                MaterialTheme.colors.favouriteButtonColor
            },
            modifier = modifier.graphicsLayer {
                scaleX = 1.1f
                scaleY = 1.1f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = stringResource(id = R.string.favorite_icon_description)
        )
    }
}