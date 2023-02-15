package levilin.moviedatabase.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import levilin.moviedatabase.ui.theme.buttonIconColor


@Composable
fun LayoutButton(icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .then(modifier),
    ) {
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colors.buttonIconColor,
            contentDescription = "LayoutButton"
        )
    }
}