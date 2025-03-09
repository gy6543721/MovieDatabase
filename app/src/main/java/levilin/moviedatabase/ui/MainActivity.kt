package levilin.moviedatabase.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import levilin.moviedatabase.ui.navigation.BottomNavView
import levilin.moviedatabase.ui.navigation.NavGraphView
import levilin.moviedatabase.ui.theme.MovieDatabaseTheme
import levilin.moviedatabase.viewmodel.MovieDatabaseViewModel

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
                
                Scaffold(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.background)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    bottomBar = {
                        BottomNavView(
                            navController = navController,
                            bottomBarState = bottomBarState
                        )
                    }
                ) { innerPadding ->
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