package levilin.moviedatabase.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import levilin.moviedatabase.ui.theme.buttonIconColor
import levilin.moviedatabase.ui.theme.indicatorGreen
import levilin.moviedatabase.ui.theme.indicatorRed
import java.util.*

@Composable
fun PercentageIndicator(percentage: Float, size: Int) {
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size.dp)) {
        CircularProgressIndicator(
            progress = animatedProgress,
            color = if (percentage < 0.6f) {
                MaterialTheme.colors.indicatorGreen
            } else if (percentage > 0.7f) {
                MaterialTheme.colors.indicatorRed
            } else {
                MaterialTheme.colors.primary
           },
            strokeWidth = (size / 12).dp
        )
        Text(text = getPercentString(percent = percentage), fontSize = (size / 5).sp, color = MaterialTheme.colors.buttonIconColor)
    }
    LaunchedEffect(percentage) {
        progress = percentage
    }
}

fun getPercentString(percent: Float): String {
    return java.lang.String.format(Locale.US, "%d%%", (percent * 100).toInt())
}

@Composable
@Preview
fun PercentageIndicatorPreview() {
    PercentageIndicator(percentage = 0.69f, size = 50)
}