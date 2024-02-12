package levilin.moviedatabase.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import levilin.moviedatabase.R
import levilin.moviedatabase.ui.component.LayoutButton
import levilin.moviedatabase.ui.component.MovieList
import levilin.moviedatabase.ui.component.SearchBar
import levilin.moviedatabase.ui.theme.screenTextColor
import levilin.moviedatabase.viewmodel.SharedViewModel

@ExperimentalComposeUiApi
@Composable
fun MovieListScreen(navController: NavController, viewModel: SharedViewModel = hiltViewModel()) {
    // Focus Control
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val searchQuery by remember { mutableStateOf(value = viewModel.searchQuery) }
    val currentPage by remember { mutableStateOf(value = viewModel.currentPage) }
    val totalPage by remember { mutableStateOf(value = viewModel.totalPage) }

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            SearchBar(
                hint = stringResource(id = R.string.search_bar_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                viewModel = viewModel,
                onSearch = { input ->
                    searchQuery.value = input
                    viewModel.loadMovieList()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Page Indicator
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LayoutButton(
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        viewModel.moveCurrentPage(value = -1)
                        focusManager.clearFocus()
                    }
                )
                Text(
                    textAlign = TextAlign.Center,
                    text = if (totalPage.value != Int.MAX_VALUE) {
                        "${currentPage.value} / ${totalPage.value}"
                    } else {
                        "- / -"
                    },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.screenTextColor
                )
                LayoutButton(
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        viewModel.moveCurrentPage(value = 1)
                        focusManager.clearFocus()
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            MovieList(navController = navController, viewModel = viewModel)
        }
    }
}