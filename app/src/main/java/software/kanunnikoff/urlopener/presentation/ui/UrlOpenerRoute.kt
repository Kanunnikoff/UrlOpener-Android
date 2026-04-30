package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import software.kanunnikoff.urlopener.presentation.UrlOpenerEvent
import software.kanunnikoff.urlopener.presentation.UrlOpenerState

@Composable
fun UrlOpenerRoute(
    state: UrlOpenerState,
    events: Flow<UrlOpenerEvent>,
    onUrlChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onOpenClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is UrlOpenerEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    UrlOpenerScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onUrlChanged = onUrlChanged,
        onClearClick = onClearClick,
        onOpenClick = onOpenClick,
    )
}
