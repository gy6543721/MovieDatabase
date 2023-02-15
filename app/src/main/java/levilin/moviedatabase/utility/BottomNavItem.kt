package levilin.moviedatabase.utility

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(var title:String, var icon: ImageVector, var screen_route:String){
    object MovieList : BottomNavItem("Movies", Icons.Default.List,"movie_list_screen")
    object FavoriteList : BottomNavItem("Favorite", Icons.Default.Favorite,"favorite_list_screen")
}