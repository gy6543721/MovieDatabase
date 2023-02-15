package levilin.moviedatabase.ui.navigation

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import levilin.moviedatabase.model.local.BottomNavItem
import levilin.moviedatabase.ui.theme.buttonBackgroundColor
import levilin.moviedatabase.ui.theme.buttonIconColor


@Composable
fun BottomNavView(navController: NavController) {
    val items = listOf(
        BottomNavItem.MovieList,
        BottomNavItem.FavoriteList
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.buttonBackgroundColor,
        contentColor = MaterialTheme.colors.buttonIconColor
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title,
                    fontSize = 9.sp) },
                selectedContentColor = MaterialTheme.colors.buttonIconColor,
                unselectedContentColor = MaterialTheme.colors.buttonIconColor.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}