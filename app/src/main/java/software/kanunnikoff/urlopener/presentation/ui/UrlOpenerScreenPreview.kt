package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.presentation.UrlOpenerState
import software.kanunnikoff.urlopener.presentation.theme.UrlOpenerTheme

/**
 * Design-time preview for the root screen with representative saved-link data.
 */
@Preview(showBackground = true)
@Composable
private fun UrlOpenerScreenPreview() {
    UrlOpenerTheme {
        UrlOpenerScreen(
            state = UrlOpenerState(
                url = "https://example.com",
                groups = listOf(
                    LinkGroup(
                        id = 1L,
                        name = "Работа",
                        description = "Служебные ссылки",
                        links = listOf(
                            SavedLink(
                                id = 1L,
                                name = "Почта",
                                url = "https://mail.example",
                            ),
                        ),
                    ),
                ),
            ),
            snackbarHostState = SnackbarHostState(),
            appVersionName = "1.0.0",
            appVersionCode = 1,
            onTabSelected = {},
            onUrlChanged = {},
            onClearClick = {},
            onOpenClick = {},
            onDeleteConfirmationChanged = {},
            onOpenConfirmationChanged = {},
            onExportJsonClick = {},
            onImportJsonClick = {},
            onSyncToDriveClick = {},
            onSyncFromDriveClick = {},
            onAddGroupClick = {},
            onEditGroupClick = {},
            onRequestDeleteGroup = {},
            onSaveGroup = { _, _ -> },
            onDismissGroupEditor = {},
            onAddLinkClick = {},
            onSaveEnteredLinkClick = {},
            onGroupPickedForEnteredLink = {},
            onDismissGroupPicker = {},
            onEditLinkClick = { _, _ -> },
            onRequestDeleteLink = { _, _ -> },
            onSaveLink = { _, _, _ -> },
            onDismissLinkEditor = {},
            onSavedLinkClick = { _, _ -> },
            onConfirmDelete = {},
            onDismissDeleteConfirmation = {},
            onConfirmOpenSavedLink = {},
            onDismissOpenConfirmation = {},
        )
    }
}
