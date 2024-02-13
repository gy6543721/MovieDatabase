package levilin.moviedatabase.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import levilin.moviedatabase.ui.navigation.BottomNavView
import levilin.moviedatabase.ui.navigation.NavGraphView
import levilin.moviedatabase.ui.theme.MovieDatabaseTheme
import levilin.moviedatabase.viewmodel.MovieDatabaseViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val movieDatabaseViewModel: MovieDatabaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDatabaseTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                
                Scaffold(bottomBar = { BottomNavView(navController = navController, bottomBarState = bottomBarState) }) { innerPadding ->
                    var paddingValue = innerPadding
                    when (navBackStackEntry?.destination?.route) {
                        "movie_list_screen" -> {
                            bottomBarState.value = true
                        }
                        "favorite_list_screen" -> {
                            bottomBarState.value = true
                        }
                        else -> {
                            bottomBarState.value = false
                            paddingValue = PaddingValues(0.dp)
                        }
                    }
                    NavGraphView(navController = navController, movieDatabaseViewModel = movieDatabaseViewModel, modifier = Modifier.padding(paddingValues = paddingValue))
                }
            }
        }
    }
}