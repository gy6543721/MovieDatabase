package levilin.moviedatabase.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import levilin.moviedatabase.model.remote.detail.MovieDetail
import levilin.moviedatabase.ui.theme.screenBackgroundColor
import levilin.moviedatabase.utility.NetworkResult
import levilin.moviedatabase.viewmodel.SharedViewModel

@Composable
fun MovieDetailScreen(navController: NavController, viewModel: SharedViewModel = hiltViewModel(), id: String) {
//    val movieDetail = produceState<NetworkResult<MovieDetail>>(initialValue = NetworkResult.Loading()) {
//        viewModel.loadMovieDetail(id = id)
//        value = viewModel.movieDetailResponse.value!!
//    }.value

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.screenBackgroundColor)
        .padding(bottom = 16.dp)
    ) {
        Text(text = id)
    }


}