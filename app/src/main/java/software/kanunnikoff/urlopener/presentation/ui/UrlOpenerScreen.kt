package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.presentation.DeleteTarget
import software.kanunnikoff.urlopener.presentation.UrlOpenerState
import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab

/**
 * Stateless root screen for UrlOpener.
 *
 * All state and callbacks are passed in from the route so this composable can be previewed and
 * tested without constructing repositories or a ViewModel.
 *
 * @param state Complete immutable screen state to render.
 * @param snackbarHostState Host state used by the route for transient snackbar messages.
 * @param appVersionName Human-readable app version shown on the About tab.
 * @param appVersionCode Numeric build version shown on the About tab.
 * @param onTabSelected Called when the user selects a bottom-navigation tab.
 * @param onUrlChanged Called when the URL input text changes.
 * @param onClearClick Called when the user clears the URL input.
 * @param onOpenClick Called when the user opens the entered URL.
 * @param onDeleteConfirmationChanged Called when the delete-confirmation setting changes.
 * @param onOpenConfirmationChanged Called when the saved-link open-confirmation setting changes.
 * @param onAddGroupClick Called when the user starts creating a group.
 * @param onEditGroupClick Called when the user starts editing a group.
 * @param onRequestDeleteGroup Called when the user requests group deletion.
 * @param onSaveGroup Called with group draft values when the group editor is confirmed.
 * @param onDismissGroupEditor Called when the group editor should be closed.
 * @param onAddLinkClick Called when the user starts adding a link to a group.
 * @param onSaveEnteredLinkClick Called when the user saves the current URL input.
 * @param onGroupPickedForEnteredLink Called with the selected group for the current URL input.
 * @param onDismissGroupPicker Called when the group picker should be closed.
 * @param onEditLinkClick Called when the user starts editing a saved link.
 * @param onRequestDeleteLink Called when the user requests saved-link deletion.
 * @param onSaveLink Called with link draft values when the link editor is confirmed.
 * @param onDismissLinkEditor Called when the link editor should be closed.
 * @param onSavedLinkClick Called when the user selects a saved link for opening.
 * @param onConfirmDelete Called when a pending deletion is confirmed.
 * @param onDismissDeleteConfirmation Called when the deletion confirmation dialog is dismissed.
 * @param onConfirmOpenSavedLink Called when opening a saved link is confirmed.
 * @param onDismissOpenConfirmation Called when the open confirmation dialog is dismissed.
 * @param modifier Optional modifier for the root scaffold.
 */
@Composable
fun UrlOpenerScreen(
    state: UrlOpenerState,
    snackbarHostState: SnackbarHostState,
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
    modifier: Modifier = Modifier,
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            UrlOpenerNavigationBar(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        when (state.selectedTab) {
            UrlOpenerTab.Home -> HomeScreen(
                state = state,
                onUrlChanged = onUrlChanged,
                onClearClick = onClearClick,
                onOpenClick = onOpenClick,
                onSaveEnteredLinkClick = onSaveEnteredLinkClick,
                onAddGroupClick = onAddGroupClick,
                onEditGroupClick = onEditGroupClick,
                onRequestDeleteGroup = onRequestDeleteGroup,
                onAddLinkClick = onAddLinkClick,
                onEditLinkClick = onEditLinkClick,
                onRequestDeleteLink = onRequestDeleteLink,
                onSavedLinkClick = onSavedLinkClick,
                contentPadding = paddingValues,
            )

            UrlOpenerTab.Settings -> SettingsScreen(
                shouldAskDeleteConfirmation = state.shouldAskDeleteConfirmation,
                shouldAskOpenConfirmation = state.shouldAskOpenConfirmation,
                onDeleteConfirmationChanged = onDeleteConfirmationChanged,
                onOpenConfirmationChanged = onOpenConfirmationChanged,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
            )

            UrlOpenerTab.About -> AboutScreen(
                appVersionName = appVersionName,
                appVersionCode = appVersionCode,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
            )
        }
    }

    state.groupEditor?.let { editor ->
        GroupEditorDialog(
            editor = editor,
            onSaveGroup = onSaveGroup,
            onDismiss = onDismissGroupEditor,
        )
    }

    state.linkEditor?.let { editor ->
        LinkEditorDialog(
            editor = editor,
            onSaveLink = onSaveLink,
            onDismiss = onDismissLinkEditor,
        )
    }

    if (state.shouldShowGroupPicker) {
        // The picker is only shown when the user wants to save the currently entered URL and more
        // than one group exists, so the screen needs an explicit destination choice.
        GroupPickerDialog(
            groups = state.groups,
            onGroupPicked = onGroupPickedForEnteredLink,
            onDismiss = onDismissGroupPicker,
        )
    }

    state.deleteTarget?.let {
        ConfirmationDialog(
            title = stringResource(R.string.delete_confirmation_title),
            text = when (it) {
                is DeleteTarget.Group -> stringResource(R.string.delete_group_confirmation)
                is DeleteTarget.Link -> stringResource(R.string.delete_link_confirmation)
            },
            confirmText = stringResource(R.string.delete_button),
            onConfirm = onConfirmDelete,
            onDismiss = onDismissDeleteConfirmation,
        )
    }

    state.openTarget?.let { target ->
        ConfirmationDialog(
            title = stringResource(R.string.open_confirmation_title),
            text = stringResource(R.string.open_link_confirmation, target.link.name),
            confirmText = stringResource(R.string.open_button),
            onConfirm = onConfirmOpenSavedLink,
            onDismiss = onDismissOpenConfirmation,
        )
    }
}
