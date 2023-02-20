package levilin.moviedatabase.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import levilin.moviedatabase.R
import levilin.moviedatabase.ui.theme.buttonIconColor
import levilin.moviedatabase.utility.clickableSingle

@Composable
fun LayoutButton(icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .clickableSingle { onClick() }
            .then(modifier),
    ) {
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colors.buttonIconColor,
            contentDescription = stringResource(id = R.string.layout_button_description)
        )
    }
}