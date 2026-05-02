package software.kanunnikoff.urlopener.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import software.kanunnikoff.urlopener.BuildConfig
import software.kanunnikoff.urlopener.presentation.theme.UrlOpenerTheme
import software.kanunnikoff.urlopener.presentation.ui.UrlOpenerRoute

/**
 * Application entry point that wires persistence, domain use cases, and the Compose screen.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: UrlOpenerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            UrlOpenerTheme {
                UrlOpenerRoute(
                    state = viewModel.state.collectAsStateWithLifecycle().value,
                    events = viewModel.eventFlow,
                    appVersionName = BuildConfig.VERSION_NAME,
                    appVersionCode = BuildConfig.VERSION_CODE,
                    onTabSelected = viewModel::onTabSelected,
                    onUrlChanged = viewModel::onUrlChanged,
                    onClearClick = viewModel::onClearClick,
                    onOpenClick = viewModel::onOpenClick,
                    onDeleteConfirmationChanged = viewModel::onDeleteConfirmationChanged,
                    onOpenConfirmationChanged = viewModel::onOpenConfirmationChanged,
                    onExportJsonClick = viewModel::onExportJsonClick,
                    onImportJsonClick = viewModel::onImportJsonClick,
                    onSyncToDriveClick = viewModel::onSyncToDriveClick,
                    onSyncFromDriveClick = viewModel::onSyncFromDriveClick,
                    onJsonImported = viewModel::onJsonImported,
                    onTransferFinished = viewModel::onTransferFinished,
                    onDriveAuthorizationCompleted = viewModel::onDriveAuthorizationCompleted,
                    onAddGroupClick = viewModel::onAddGroupClick,
                    onEditGroupClick = viewModel::onEditGroupClick,
                    onRequestDeleteGroup = viewModel::onRequestDeleteGroup,
                    onSaveGroup = viewModel::onSaveGroup,
                    onDismissGroupEditor = viewModel::onDismissGroupEditor,
                    onAddLinkClick = viewModel::onAddLinkClick,
                    onSaveEnteredLinkClick = viewModel::onSaveEnteredLinkClick,
                    onGroupPickedForEnteredLink = viewModel::onGroupPickedForEnteredLink,
                    onDismissGroupPicker = viewModel::onDismissGroupPicker,
                    onEditLinkClick = viewModel::onEditLinkClick,
                    onRequestDeleteLink = viewModel::onRequestDeleteLink,
                    onSaveLink = viewModel::onSaveLink,
                    onDismissLinkEditor = viewModel::onDismissLinkEditor,
                    onSavedLinkClick = viewModel::onSavedLinkClick,
                    onConfirmDelete = viewModel::onConfirmDelete,
                    onDismissDeleteConfirmation = viewModel::onDismissDeleteConfirmation,
                    onConfirmOpenSavedLink = viewModel::onConfirmOpenSavedLink,
                    onDismissOpenConfirmation = viewModel::onDismissOpenConfirmation,
                )
            }
        }
    }
}
