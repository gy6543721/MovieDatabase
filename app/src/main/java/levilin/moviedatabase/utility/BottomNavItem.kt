package levilin.moviedatabase.utility

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String){
    data object MovieList : BottomNavItem(title = ConstantValue.NAV_ITEM_MOVIES, icon = Icons.AutoMirrored.Filled.List, route = "movie_list_screen")
    data object FavoriteList : BottomNavItem(title = ConstantValue.NAV_ITEM_Favorite, icon = Icons.Default.Favorite, route = "favorite_list_screen")
}