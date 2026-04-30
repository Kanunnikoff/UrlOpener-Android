package software.kanunnikoff.urlopener.presentation.ui

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
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
    onOpenConfirmationChanged: (Boolean) -> Unit,
    onAddGroupClick: () -> Unit,
    onEditGroupClick: (LinkGroup) -> Unit,
    onRequestDeleteGroup: (Long) -> Unit,
    onSaveGroup: (String, String) -> Unit,
    onDismissGroupEditor: () -> Unit,
    onAddLinkClick: (Long) -> Unit,
    onSaveEnteredLinkClick: () -> Unit,
    onGroupPickedForEnteredLink: (Long) -> Unit,
    onDismissGroupPicker: () -> Unit,
    onEditLinkClick: (Long, SavedLink) -> Unit,
    onRequestDeleteLink: (Long, Long) -> Unit,
    onSaveLink: (Long, String, String) -> Unit,
    onDismissLinkEditor: () -> Unit,
    onSavedLinkClick: (Long, Long) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDeleteConfirmation: () -> Unit,
    onConfirmOpenSavedLink: () -> Unit,
    onDismissOpenConfirmation: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val openUrlFailedMessage = stringResource(R.string.open_url_failed_message)

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                UrlOpenerEvent.OpenUrlFailed -> {
                    Toast.makeText(context, openUrlFailedMessage, Toast.LENGTH_SHORT).show()
                }

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
        onOpenConfirmationChanged = onOpenConfirmationChanged,
        onAddGroupClick = onAddGroupClick,
        onEditGroupClick = onEditGroupClick,
        onRequestDeleteGroup = onRequestDeleteGroup,
        onSaveGroup = onSaveGroup,
        onDismissGroupEditor = onDismissGroupEditor,
        onAddLinkClick = onAddLinkClick,
        onSaveEnteredLinkClick = onSaveEnteredLinkClick,
        onGroupPickedForEnteredLink = onGroupPickedForEnteredLink,
        onDismissGroupPicker = onDismissGroupPicker,
        onEditLinkClick = onEditLinkClick,
        onRequestDeleteLink = onRequestDeleteLink,
        onSaveLink = onSaveLink,
        onDismissLinkEditor = onDismissLinkEditor,
        onSavedLinkClick = onSavedLinkClick,
        onConfirmDelete = onConfirmDelete,
        onDismissDeleteConfirmation = onDismissDeleteConfirmation,
        onConfirmOpenSavedLink = onConfirmOpenSavedLink,
        onDismissOpenConfirmation = onDismissOpenConfirmation,
    )
}
