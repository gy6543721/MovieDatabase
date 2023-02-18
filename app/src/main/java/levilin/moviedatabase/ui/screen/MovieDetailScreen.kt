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
    val movieDetail by remember { mutableStateOf(value = viewModel.movieDetail) }
    val loadingErrorMessage by remember { mutableStateOf(value = viewModel.errorMovieDetailMessage) }
    val isLoading by remember { mutableStateOf(value = viewModel.isMovieDetailLoading) }

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
                if (movieDetail.value.adult) {
                    Text(text = "18+", color = MaterialTheme.colors.indicatorRed, modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
                }
            }
            if (isLoading.value) {
                // Loading Indicator & Retry Section
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize().padding(10.dp)) {
                    if(loadingErrorMessage.value.isBlank()) {
                        CircularProgressIndicator(color = MaterialTheme.colors.primary)
                    } else {
                        RetrySection(error = loadingErrorMessage.value, onRetry = {
                            viewModel.loadMovieDetail(id = movieDetail.value.id.toString())
                        })
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // English Title
                    Text(text = movieDetail.value.title, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    // Original Title
                    Text(text = movieDetail.value.originalTitle, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
                    // Poster
                    LoadableAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(ConstantValue.IMAGE_BASE_URL + movieDetail.value.posterPath)
                            .crossfade(true)
                            .build(),
                        contentDescription = movieDetail.value.title,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(10.dp),
                        alignment = Alignment.Center
                    )
                    // Vote Indicator
                    PercentageIndicator(percentage = movieDetail.value.voteAverage.toFloat() / 10f, size = 60)
                    // Genres
                    if (movieDetail.value.genres.isNotEmpty()) {
                        var genresString = ""
                        for (i in movieDetail.value.genres.indices) {
                            genresString += movieDetail.value.genres[i].name
                            if (i < movieDetail.value.genres.size-1) {
                                genresString += " / "
                            }
                        }
                        Text(text = genresString, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                    }
                    // Release Date
                    Text(text = movieDetail.value.releaseDate, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
                    // Overview
                    Text(text = movieDetail.value.overview, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Normal)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Language
                    if (movieDetail.value.spokenLanguages.isNotEmpty()) {
                        var languageString = "・Language :  \n"
                        for (i in movieDetail.value.spokenLanguages.indices) {
                            languageString += "\u0020 \u0020 ${movieDetail.value.spokenLanguages[i].englishName}"
                            if (movieDetail.value.spokenLanguages[i].name.isNotEmpty() || movieDetail.value.spokenLanguages[i].name != "") {
                                languageString += "\u0020 (${movieDetail.value.spokenLanguages[i].name})"
                            }
                            if (i < movieDetail.value.spokenLanguages.size-1) {
                                languageString += "\n"
                            }
                        }
                        Text(text = languageString, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Normal)
                    }
                    // Production Company
                    if (movieDetail.value.productionCompanies.isNotEmpty()) {
                        var producerString = "・Production Company : \n"
                        for (i in movieDetail.value.productionCompanies.indices) {
                            producerString += "\u0020 \u0020 ${movieDetail.value.productionCompanies[i].name}"
                            if (movieDetail.value.productionCompanies[i].originCountry.isNotEmpty() || movieDetail.value.productionCompanies[i].originCountry != "") {
                                producerString += "\u0020 (${movieDetail.value.productionCompanies[i].originCountry})"
                            }
                            if (i < movieDetail.value.productionCompanies.size-1) {
                                producerString += "\n"
                            }
                        }
                        Text(text = producerString, color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Normal)
                    }
                    // Budget & Revenue
                    if (movieDetail.value.budget != 0L || movieDetail.value.revenue != 0L) {
                        val budgetString = if (movieDetail.value.budget != 0L) {
                            NumberFormat.getCurrencyInstance(Locale.US).format(movieDetail.value.budget).split(".")[0]
                        } else {
                            "-"
                        }
                        val revenueString = if (movieDetail.value.revenue != 0L) {
                            NumberFormat.getCurrencyInstance(Locale.US).format(movieDetail.value.revenue).split(".")[0]
                        } else {
                            "-"
                        }
                        Text(text = "・Budget / Revenue :   \n \u0020 \u0020 $budgetString / $revenueString", color = MaterialTheme.colors.screenTextColor, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), fontWeight = FontWeight.Normal)
                    }
                }
            }
        }
    }
}