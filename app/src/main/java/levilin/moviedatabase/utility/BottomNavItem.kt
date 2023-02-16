package levilin.moviedatabase.utility

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String){
    object MovieList : BottomNavItem(title = ConstantValue.NAV_ITEM_MOVIES, icon = Icons.Default.List, route = "movie_list_screen")
    object FavoriteList : BottomNavItem(title = ConstantValue.NAV_ITEM_Favorite, icon = Icons.Default.Favorite, route = "favorite_list_screen")
}