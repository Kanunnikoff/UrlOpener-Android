package software.kanunnikoff.urlopener.presentation.ui

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.nio.charset.StandardCharsets
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.presentation.TransferMessage
import software.kanunnikoff.urlopener.presentation.UrlOpenerEvent
import software.kanunnikoff.urlopener.presentation.UrlOpenerState
import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab

/**
 * Connects ViewModel state and one-time events to the stateless screen.
 *
 * This layer owns Android-only feedback such as Toasts while leaving [UrlOpenerScreen] reusable for
 * previews and UI tests.
 *
 * @param state Complete immutable screen state to render.
 * @param events One-time UI events emitted by the ViewModel.
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
 */
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
    onExportJsonClick: () -> Unit,
    onImportJsonClick: () -> Unit,
    onSyncToDriveClick: () -> Unit,
    onSyncFromDriveClick: () -> Unit,
    onJsonImported: (String, TransferMessage, TransferMessage) -> Unit,
    onTransferFinished: (TransferMessage) -> Unit,
    onDriveAuthorizationCompleted: (Boolean) -> Unit,
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
    var pendingExportJson by remember { mutableStateOf<String?>(null) }

    val driveAuthorizationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        onDriveAuthorizationCompleted(result.resultCode == Activity.RESULT_OK)
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(JSON_MIME_TYPE),
    ) { uri ->
        val json = pendingExportJson
        pendingExportJson = null

        if (uri == null || json == null) {
            return@rememberLauncherForActivityResult
        }

        val message = if (context.writeText(uri, json)) {
            TransferMessage.ExportCompleted
        } else {
            TransferMessage.ExportFailed
        }

        onTransferFinished(message)
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri == null) {
            return@rememberLauncherForActivityResult
        }

        val json = context.readText(uri)

        if (json == null) {
            onTransferFinished(TransferMessage.ImportFailed)
        } else {
            onJsonImported(
                json,
                TransferMessage.ImportCompleted,
                TransferMessage.ImportFailed,
            )
        }
    }

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                UrlOpenerEvent.OpenUrlFailed -> {
                    Toast.makeText(context, openUrlFailedMessage, Toast.LENGTH_SHORT).show()
                }

                is UrlOpenerEvent.ShowError -> snackbarHostState.showSnackbar(event.message)

                is UrlOpenerEvent.ExportJsonRequested -> {
                    pendingExportJson = event.json
                    exportLauncher.launch(event.fileName)
                }

                UrlOpenerEvent.ImportJsonRequested -> importLauncher.launch(arrayOf(JSON_MIME_TYPE))

                is UrlOpenerEvent.RequestDriveAuthorization -> {
                    driveAuthorizationLauncher.launch(
                        IntentSenderRequest.Builder(event.pendingIntent).build(),
                    )
                }

                is UrlOpenerEvent.ShowTransferMessage -> snackbarHostState.showSnackbar(
                    context.getTransferMessage(event.message),
                )
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
        onExportJsonClick = onExportJsonClick,
        onImportJsonClick = onImportJsonClick,
        onSyncToDriveClick = onSyncToDriveClick,
        onSyncFromDriveClick = onSyncFromDriveClick,
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

private const val JSON_MIME_TYPE = "application/json"

private fun android.content.Context.writeText(uri: Uri, text: String): Boolean {
    return try {
        contentResolver.openOutputStream(uri)?.use { output ->
            output.write(text.toByteArray(StandardCharsets.UTF_8))
        } != null
    } catch (exception: IOException) {
        false
    } catch (exception: SecurityException) {
        false
    }
}

private fun android.content.Context.readText(uri: Uri): String? {
    return try {
        contentResolver.openInputStream(uri)?.use { input ->
            input.readBytes().toString(StandardCharsets.UTF_8)
        }
    } catch (exception: IOException) {
        null
    } catch (exception: SecurityException) {
        null
    }
}

private fun android.content.Context.getTransferMessage(message: TransferMessage): String {
    val stringRes = when (message) {
        TransferMessage.ExportCompleted -> R.string.export_completed_message
        TransferMessage.ExportFailed -> R.string.export_failed_message
        TransferMessage.ImportCompleted -> R.string.import_completed_message
        TransferMessage.ImportFailed -> R.string.import_failed_message
        TransferMessage.DriveSyncCompleted -> R.string.drive_sync_completed_message
        TransferMessage.DriveSyncFailed -> R.string.drive_sync_failed_message
        TransferMessage.DriveImportCompleted -> R.string.drive_import_completed_message
        TransferMessage.DriveImportFailed -> R.string.drive_import_failed_message
    }

    return getString(stringRes)
}
