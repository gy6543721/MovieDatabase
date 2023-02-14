package levilin.moviedatabase.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import levilin.moviedatabase.R
import levilin.moviedatabase.model.remote.list.MovieResult
import levilin.moviedatabase.ui.component.MovieCard
import levilin.moviedatabase.ui.theme.buttonBackgroundColor
import levilin.moviedatabase.ui.theme.buttonIconColor
import levilin.moviedatabase.viewmodel.SharedViewModel

@Composable
fun MovieListScreen(navController: NavController, viewModel: SharedViewModel = hiltViewModel()) {
    // Focus Control
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var searchQuery by remember { viewModel.searchQuery }
    var currentPage by remember { viewModel.currentPage }
    val totalPage by remember { viewModel.totalPage }

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
            ) { input ->
                searchQuery = input
                currentPage = 1
                viewModel.loadMoviesList()
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (currentPage > 1) {
                            currentPage -= 1
                            viewModel.loadMoviesList()
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.buttonBackgroundColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colors.buttonIconColor,
                        contentDescription = "Previous Page"
                    )
                }

                Text(
                    textAlign = TextAlign.Center,
                    text = if (totalPage != Int.MAX_VALUE) {
                        "$currentPage / $totalPage"
                    } else {
                        "- / -"
                    }
                )

                Button(
                    onClick = {
                        if (currentPage < totalPage) {
                            currentPage += 1
                            viewModel.loadMoviesList()
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.buttonBackgroundColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        tint = MaterialTheme.colors.buttonIconColor,
                        contentDescription = "Next Page"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MoviesList(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, hint: String = "", onSearch: (String) -> Unit = {}) {
    var inputText by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = modifier) {
        BasicTextField(
            value = inputText,
            onValueChange = { inputValue ->
                inputText = inputValue
                onSearch(inputValue)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged { focusState ->
                    isHintDisplayed = !focusState.isFocused && inputText.isEmpty()
                }
        )
        if(isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun MoviesList(navController: NavController, viewModel: SharedViewModel = hiltViewModel()) {
    val moviesList by remember { viewModel.moviesList }
    val loadingError by remember { viewModel.loadingError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if(moviesList.size % 2 == 0) {
            moviesList.size / 2
        } else {
            moviesList.size / 2 + 1
        }
        items(itemCount) { rowIndex ->
            if(rowIndex >= itemCount - 1) {
                LaunchedEffect(key1 = true) {
                    viewModel.loadMoviesList()
                }
            }
            ListRow(rowIndex = rowIndex, entries = moviesList, navController = navController, viewModel = viewModel)
        }
    }
    Box(contentAlignment = Center, modifier = Modifier.fillMaxSize()) {
        if(isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(loadingError.isNotEmpty()) {
            RetrySection(error = loadingError) {
                viewModel.loadMoviesList()
            }
        }
    }
}

@Composable
fun ListRow(rowIndex: Int, entries: List<MovieResult>, navController: NavController, viewModel: SharedViewModel = hiltViewModel()) {
    Column {
        Row {
            MovieCard(
                entry = entries[rowIndex * 2],
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2) {
                MovieCard(
                    entry = entries[rowIndex * 2 + 1],
                    viewModel = viewModel,
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}