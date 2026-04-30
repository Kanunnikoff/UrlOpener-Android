package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import software.kanunnikoff.urlopener.presentation.UrlOpenerEvent
import software.kanunnikoff.urlopener.presentation.UrlOpenerState
import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab

@Composable
fun UrlOpenerRoute(
    state: UrlOpenerState,
    events: Flow<UrlOpenerEvent>,
    appVersionName: String,
    appVersionCode: Int,
    onTabSelected: (UrlOpenerTab) -> Unit,
    onUrlChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onOpenClick: () -> Unit,
    onDeleteConfirmationChanged: (Boolean) -> Unit,
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
        appVersionName = appVersionName,
        appVersionCode = appVersionCode,
        onTabSelected = onTabSelected,
        onUrlChanged = onUrlChanged,
        onClearClick = onClearClick,
        onOpenClick = onOpenClick,
        onDeleteConfirmationChanged = onDeleteConfirmationChanged,
    )
}
