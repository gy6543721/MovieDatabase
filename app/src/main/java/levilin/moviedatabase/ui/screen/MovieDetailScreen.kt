package levilin.moviedatabase.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import levilin.moviedatabase.ui.component.LayoutButton
import levilin.moviedatabase.ui.component.LoadableAsyncImage
import levilin.moviedatabase.ui.component.PercentageIndicator
import levilin.moviedatabase.ui.component.RetrySection
import levilin.moviedatabase.ui.theme.indicatorRed
import levilin.moviedatabase.ui.theme.screenBackgroundColor
import levilin.moviedatabase.ui.theme.screenTextColor
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.viewmodel.SharedViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun MovieDetailScreen(navController: NavController, viewModel: SharedViewModel = hiltViewModel()) {
    val movieDetail by remember { viewModel.movieDetail }
    val loadingError by remember { viewModel.loadingError }
    val isLoading by remember { viewModel.isRemoteLoading }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.screenBackgroundColor)
        .verticalScroll(rememberScrollState())
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Back Button
                LayoutButton(icon = Icons.Default.ArrowBack, modifier = Modifier.size(32.dp), onClick = { navController.popBackStack() })
                // Adult
                if (movieDetail.adult) {
                    Text(text = "18+", color = MaterialTheme.colors.indicatorRed, modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // English Title
                Text(text = movieDetail.title, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                // Original Title
                Text(text = movieDetail.originalTitle, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
                // Poster
                LoadableAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(ConstantValue.IMAGE_BASE_URL + movieDetail.posterPath)
                        .crossfade(true)
                        .build(),
                    contentDescription = movieDetail.title,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(10.dp),
                    alignment = Alignment.Center
                )
                // Vote Indicator
                PercentageIndicator(percentage = movieDetail.voteAverage.toFloat() / 10f, size = 60)
                // Genres
                if (movieDetail.genres.isNotEmpty()) {
                    var genresString = ""
                    for (i in movieDetail.genres.indices) {
                        genresString += movieDetail.genres[i].name
                        if (i < movieDetail.genres.size-1) {
                            genresString += " / "
                        }
                    }
                    Text(text = genresString, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                }
                // Release Date
                Text(text = movieDetail.releaseDate, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
                // Overview
                Text(text = movieDetail.overview, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Normal)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Language
                if (movieDetail.spokenLanguages.isNotEmpty()) {
                    var languageString = "・Language :  \n"
                    for (i in movieDetail.spokenLanguages.indices) {
                        languageString += "\u0020 \u0020 ${movieDetail.spokenLanguages[i].englishName}"
                        if (movieDetail.spokenLanguages[i].name.isNotEmpty() || movieDetail.spokenLanguages[i].name != "") {
                            languageString += "\u0020 (${movieDetail.spokenLanguages[i].name})"
                        }
                        if (i < movieDetail.spokenLanguages.size-1) {
                            languageString += "\n"
                        }
                    }
                    Text(text = languageString, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Normal)
                }
                // Production Company
                if (movieDetail.productionCompanies.isNotEmpty()) {
                    var producerString = "・Production Company : \n"
                    for (i in movieDetail.productionCompanies.indices) {
                        producerString += "\u0020 \u0020 ${movieDetail.productionCompanies[i].name}"
                        if (movieDetail.productionCompanies[i].originCountry.isNotEmpty() || movieDetail.productionCompanies[i].originCountry != "") {
                            producerString += "\u0020 (${movieDetail.productionCompanies[i].originCountry})"
                        }
                        if (i < movieDetail.productionCompanies.size-1) {
                            producerString += "\n"
                        }
                    }
                    Text(text = producerString, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Normal)
                }
                // Budget & Revenue
                if (movieDetail.budget != 0L || movieDetail.revenue != 0L) {
                    val budgetString = if (movieDetail.budget != 0L) {
                        NumberFormat.getCurrencyInstance(Locale.US).format(movieDetail.budget).split(".")[0]
                    } else {
                        "-"
                    }
                    val revenueString = if (movieDetail.revenue != 0L) {
                        NumberFormat.getCurrencyInstance(Locale.US).format(movieDetail.revenue).split(".")[0]
                    } else {
                        "-"
                    }
                    Text(text = "・Budget / Revenue :   \n \u0020 \u0020 $budgetString / $revenueString", color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Normal)
                }
            }
        }
    }
}