package levilin.moviedatabase.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import levilin.moviedatabase.R
import levilin.moviedatabase.ui.theme.buttonIconColor
import levilin.moviedatabase.ui.theme.indicatorRed

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(text = error, color = MaterialTheme.colors.indicatorRed, fontSize = 15.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.retry_button), color = MaterialTheme.colors.buttonIconColor)
        }
    }
}