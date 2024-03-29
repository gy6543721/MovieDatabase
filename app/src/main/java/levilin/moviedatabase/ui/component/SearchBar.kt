package levilin.moviedatabase.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import levilin.moviedatabase.R
import levilin.moviedatabase.ui.theme.buttonIconColor
import levilin.moviedatabase.ui.theme.screenTextColor
import levilin.moviedatabase.ui.theme.searchBarBorderColor
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.viewmodel.MovieDatabaseViewModel

@Composable
fun SearchBar(modifier: Modifier = Modifier, hint: String = "", viewModel: MovieDatabaseViewModel, onSearch: (String) -> Unit = {}) {
    // Focus Control
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Search Query
    val searchQuery by remember { mutableStateOf(value = viewModel.searchQuery) }
    val inputText by remember { mutableStateOf(value = viewModel.displayQuery) }
    var isHintDisplayed by remember { mutableStateOf(value = hint != "") }

    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                isHintDisplayed = true
                inputText.value = ""
                searchQuery.value = ConstantValue.DEFAULT_QUERY
                viewModel.loadMovieList()
            },
            modifier = Modifier.padding(end = 5.dp)
        ) {
            Icon(
                modifier = Modifier.alpha(ContentAlpha.medium),
                imageVector = Icons.Filled.Close,
                tint = MaterialTheme.colors.buttonIconColor,
                contentDescription = stringResource(id = R.string.search_trailing_icon_description)
            )
        }
    }

    Box(modifier = modifier) {
        TextField(
            value = inputText.value,
            onValueChange = { inputValue ->
                inputText.value = inputValue
                if (inputValue.isBlank()) {
                    onSearch(ConstantValue.DEFAULT_QUERY)
                } else {
                    isHintDisplayed = false
                    onSearch(inputValue)
                }
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = MaterialTheme.colors.screenTextColor, textAlign = TextAlign.Start, fontSize = 15.sp),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(color = MaterialTheme.colors.background)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colors.searchBarBorderColor
                    ), shape = RoundedCornerShape(50)
                )
                .align(Alignment.Center)
                .onFocusChanged { focusState ->
                    isHintDisplayed = !focusState.isFocused && inputText.value.isBlank()
                    if (!focusState.isFocused) {
                        keyboardController?.hide()
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.loadMovieList()
                    focusManager.clearFocus()
                }
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            trailingIcon = if (inputText.value.isNotEmpty()) trailingIconView else null
        )
        if(isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Start
            )
        }
    }
}