package levilin.moviedatabase.utility

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import levilin.moviedatabase.R

sealed class BottomNavItem(var titleStringID: Int, var icon: ImageVector, var route: String){
    data object MovieList : BottomNavItem(titleStringID = R.string.nav_item_movies, icon = Icons.AutoMirrored.Filled.List, route = "movie_list_screen")
    data object FavoriteList : BottomNavItem(titleStringID = R.string.nav_item_favorite, icon = Icons.Default.Favorite, route = "favorite_list_screen")
}