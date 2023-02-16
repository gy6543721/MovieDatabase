package levilin.moviedatabase.ui.component

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun LoadableAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    loadingIndicatorSize: Dp = 20.dp,
    contentScale: ContentScale = ContentScale.Fit,
) {
    var isLoading by rememberSaveable(model) { mutableStateOf(true) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            onSuccess = { isLoading = false },
            alignment = alignment
        )
        // Loading Indicator
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(loadingIndicatorSize),
                color = MaterialTheme.colors.primary
            )
        }
    }
}