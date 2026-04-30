package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.presentation.UrlOpenerState
import software.kanunnikoff.urlopener.presentation.theme.UrlOpenerTheme

@Composable
fun UrlOpenerScreen(
    state: UrlOpenerState,
    snackbarHostState: SnackbarHostState,
    onUrlChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onOpenClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = state.url,
                onValueChange = onUrlChanged,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(stringResource(R.string.url_input_description)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                ),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(onClick = onClearClick) {
                    Text(stringResource(R.string.clear_button))
                }

                Button(onClick = onOpenClick) {
                    Text(stringResource(R.string.open_button))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UrlOpenerScreenPreview() {
    UrlOpenerTheme {
        UrlOpenerScreen(
            state = UrlOpenerState(url = "https://example.com"),
            snackbarHostState = SnackbarHostState(),
            onUrlChanged = {},
            onClearClick = {},
            onOpenClick = {},
        )
    }
}
