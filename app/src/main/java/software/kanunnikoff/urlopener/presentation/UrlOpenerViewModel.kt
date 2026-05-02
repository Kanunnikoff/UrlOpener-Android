package software.kanunnikoff.urlopener.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.domain.repository.DriveAuthorizationRequiredException
import software.kanunnikoff.urlopener.domain.usecase.AddLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.AddSavedLinkUseCase
import software.kanunnikoff.urlopener.domain.usecase.DeleteLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.DeleteSavedLinkUseCase
import software.kanunnikoff.urlopener.domain.usecase.ExportLinkGroupsJsonUseCase
import software.kanunnikoff.urlopener.domain.usecase.ExportBackupUseCase
import software.kanunnikoff.urlopener.domain.usecase.ImportBackupUseCase
import software.kanunnikoff.urlopener.domain.usecase.ImportLinkGroupsJsonUseCase
import software.kanunnikoff.urlopener.domain.usecase.ObserveLinkGroupsUseCase
import software.kanunnikoff.urlopener.domain.usecase.ObserveSettingsUseCase
import software.kanunnikoff.urlopener.domain.usecase.OpenUrlUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetDeleteConfirmationUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetOpenConfirmationUseCase
import software.kanunnikoff.urlopener.domain.usecase.UpdateLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.UpdateSavedLinkUseCase
import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab
import javax.inject.Inject

/**
 * Coordinates user actions, persistent settings, saved links, and UI state.
 *
 * Transient UI work is represented in [UrlOpenerState] with request identifiers. The UI confirms
 * handling each request so actions are not replayed by ordinary recomposition.
 */
@HiltViewModel
class UrlOpenerViewModel @Inject constructor(
    private val openUrlUseCase: OpenUrlUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val setDeleteConfirmationUseCase: SetDeleteConfirmationUseCase,
    private val setOpenConfirmationUseCase: SetOpenConfirmationUseCase,
    private val observeLinkGroupsUseCase: ObserveLinkGroupsUseCase,
    private val addLinkGroupUseCase: AddLinkGroupUseCase,
    private val updateLinkGroupUseCase: UpdateLinkGroupUseCase,
    private val deleteLinkGroupUseCase: DeleteLinkGroupUseCase,
    private val addSavedLinkUseCase: AddSavedLinkUseCase,
    private val updateSavedLinkUseCase: UpdateSavedLinkUseCase,
    private val deleteSavedLinkUseCase: DeleteSavedLinkUseCase,
    private val exportLinkGroupsJsonUseCase: ExportLinkGroupsJsonUseCase,
    private val importLinkGroupsJsonUseCase: ImportLinkGroupsJsonUseCase,
    private val exportBackupUseCase: ExportBackupUseCase,
    private val importBackupUseCase: ImportBackupUseCase,
) : ViewModel() {

    private val localState = MutableStateFlow(UrlOpenerState())

    val uiState: StateFlow<UrlOpenerState> = combine(
        localState,
        observeSettingsUseCase(),
        observeLinkGroupsUseCase(),
    ) { state, settings, groups ->
        state.copy(
            shouldAskDeleteConfirmation = settings.shouldAskDeleteConfirmation,
            shouldAskOpenConfirmation = settings.shouldAskOpenConfirmation,
            groups = groups,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UrlOpenerState(),
    )

    private var pendingDriveAction: DriveAction? = null
    private var nextRequestId = INITIAL_REQUEST_ID

    fun onTabSelected(tab: UrlOpenerTab) {
        localState.update { it.copy(selectedTab = tab) }
    }

    fun onUrlChanged(url: String) {
        localState.update { it.copy(url = url) }
    }

    fun onClearClick() {
        localState.update { it.copy(url = "") }
    }

    fun onOpenClick() {
        openUrl(uiState.value.url)
    }

    fun onDeleteConfirmationChanged(shouldAsk: Boolean) {
        viewModelScope.launch {
            setDeleteConfirmationUseCase(shouldAsk)
        }
    }

    fun onOpenConfirmationChanged(shouldAsk: Boolean) {
        viewModelScope.launch {
            setOpenConfirmationUseCase(shouldAsk)
        }
    }

    fun onExportJsonClick() {
        val request = ExportJsonRequest(
            id = nextRequestId(),
            fileName = EXPORT_FILE_NAME,
            json = exportLinkGroupsJsonUseCase(uiState.value.groups),
        )

        localState.update {
            it.copy(
                exportJsonRequest = request,
            )
        }
    }

    fun onImportJsonClick() {
        val requestId = nextRequestId()

        localState.update { it.copy(importJsonRequestId = requestId) }
    }

    fun onSyncToDriveClick() {
        performDriveAction(DriveAction.Export)
    }

    fun onSyncFromDriveClick() {
        performDriveAction(DriveAction.Import)
    }

    fun onDriveAuthorizationCompleted(success: Boolean) {
        val action = pendingDriveAction
        pendingDriveAction = null

        if (success && action != null) {
            performDriveAction(action)
            return
        }

        viewModelScope.launch {
            showTransferMessage(
                action?.failureMessage ?: TransferMessage.DriveSyncFailed,
            )
        }
    }

    fun onJsonImported(json: String, successMessage: TransferMessage, failureMessage: TransferMessage) {
        viewModelScope.launch {
            runCatching {
                importLinkGroupsJsonUseCase(json)
            }.onSuccess {
                showTransferMessage(successMessage)
            }.onFailure {
                showTransferMessage(failureMessage)
            }
        }
    }

    fun onTransferFinished(message: TransferMessage) {
        showTransferMessage(message)
    }

    fun onExportJsonRequestHandled(requestId: Long) {
        localState.update {
            if (it.exportJsonRequest?.id == requestId) {
                it.copy(exportJsonRequest = null)
            } else {
                it
            }
        }
    }

    fun onImportJsonRequestHandled(requestId: Long) {
        localState.update {
            if (it.importJsonRequestId == requestId) {
                it.copy(importJsonRequestId = null)
            } else {
                it
            }
        }
    }

    fun onDriveAuthorizationRequestHandled(requestId: Long) {
        localState.update {
            if (it.driveAuthorizationRequest?.id == requestId) {
                it.copy(driveAuthorizationRequest = null)
            } else {
                it
            }
        }
    }

    fun onUserMessageShown(messageId: Long) {
        localState.update {
            if (it.userMessage?.id == messageId) {
                it.copy(userMessage = null)
            } else {
                it
            }
        }
    }

    fun onAddGroupClick() {
        localState.update { it.copy(groupEditor = GroupEditorState()) }
    }

    fun onEditGroupClick(group: LinkGroup) {
        localState.update {
            it.copy(
                groupEditor = GroupEditorState(
                    groupId = group.id,
                    name = group.name,
                    description = group.description,
                ),
            )
        }
    }

    fun onDismissGroupEditor() {
        localState.update { it.copy(groupEditor = null) }
    }

    fun onSaveGroup(name: String, description: String) {
        val editor = uiState.value.groupEditor

        viewModelScope.launch {
            if (editor?.groupId == null) {
                addLinkGroupUseCase(name, description)
            } else {
                updateLinkGroupUseCase(editor.groupId, name, description)
            }

            localState.update { it.copy(groupEditor = null) }
        }
    }

    fun onRequestDeleteGroup(groupId: Long) {
        if (uiState.value.shouldAskDeleteConfirmation) {
            localState.update { it.copy(deleteTarget = DeleteTarget.Group(groupId)) }
        } else {
            deleteGroup(groupId)
        }
    }

    fun onAddLinkClick(groupId: Long) {
        localState.update { it.copy(linkEditor = LinkEditorState(groupId = groupId, url = it.url)) }
    }

    fun onSaveEnteredLinkClick() {
        val groups = uiState.value.groups

        when (groups.size) {
            0 -> localState.update { it.copy(groupEditor = GroupEditorState()) }
            1 -> onAddLinkClick(groups.first().id)
            else -> localState.update { it.copy(shouldShowGroupPicker = true) }
        }
    }

    fun onGroupPickedForEnteredLink(groupId: Long) {
        localState.update {
            it.copy(
                shouldShowGroupPicker = false,
                linkEditor = LinkEditorState(groupId = groupId, url = it.url),
            )
        }
    }

    fun onDismissGroupPicker() {
        localState.update { it.copy(shouldShowGroupPicker = false) }
    }

    fun onEditLinkClick(groupId: Long, link: SavedLink) {
        localState.update {
            it.copy(
                linkEditor = LinkEditorState(
                    groupId = groupId,
                    linkId = link.id,
                    name = link.name,
                    url = link.url,
                ),
            )
        }
    }

    fun onDismissLinkEditor() {
        localState.update { it.copy(linkEditor = null) }
    }

    fun onSaveLink(groupId: Long, name: String, url: String) {
        val editor = uiState.value.linkEditor

        viewModelScope.launch {
            if (editor?.linkId == null) {
                addSavedLinkUseCase(groupId, name, url)
            } else {
                updateSavedLinkUseCase(groupId, editor.linkId, name, url)
            }

            localState.update { it.copy(linkEditor = null) }
        }
    }

    fun onRequestDeleteLink(groupId: Long, linkId: Long) {
        if (uiState.value.shouldAskDeleteConfirmation) {
            localState.update { it.copy(deleteTarget = DeleteTarget.Link(groupId, linkId)) }
        } else {
            deleteLink(groupId, linkId)
        }
    }

    fun onDismissDeleteConfirmation() {
        localState.update { it.copy(deleteTarget = null) }
    }

    fun onConfirmDelete() {
        when (val target = uiState.value.deleteTarget) {
            is DeleteTarget.Group -> deleteGroup(target.groupId)
            is DeleteTarget.Link -> deleteLink(target.groupId, target.linkId)
            null -> Unit
        }
    }

    fun onSavedLinkClick(groupId: Long, linkId: Long) {
        val link = uiState.value.groups
            .firstOrNull { it.id == groupId }
            ?.links
            ?.firstOrNull { it.id == linkId }
            ?: return

        if (uiState.value.shouldAskOpenConfirmation) {
            localState.update { it.copy(openTarget = OpenTarget(link)) }
        } else {
            openUrl(link.url)
        }
    }

    fun onDismissOpenConfirmation() {
        localState.update { it.copy(openTarget = null) }
    }

    fun onConfirmOpenSavedLink() {
        val link = uiState.value.openTarget?.link ?: return
        localState.update { it.copy(openTarget = null) }
        openUrl(link.url)
    }

    private fun deleteGroup(groupId: Long) {
        viewModelScope.launch {
            deleteLinkGroupUseCase(groupId)
            localState.update { it.copy(deleteTarget = null) }
        }
    }

    private fun deleteLink(groupId: Long, linkId: Long) {
        viewModelScope.launch {
            deleteSavedLinkUseCase(groupId, linkId)
            localState.update { it.copy(deleteTarget = null) }
        }
    }

    private fun openUrl(url: String) {
        viewModelScope.launch {
            val result = openUrlUseCase(url)

            if (result.isFailure) {
                val message = UserMessage(
                    id = nextRequestId(),
                    kind = UserMessageKind.OpenUrlFailed,
                )

                localState.update {
                    it.copy(
                        userMessage = message,
                    )
                }
            }
        }
    }

    private fun performDriveAction(action: DriveAction) {
        viewModelScope.launch {
            val result = when (action) {
                DriveAction.Export -> exportBackupUseCase()
                DriveAction.Import -> importBackupUseCase()
            }

            result.fold(
                onSuccess = {
                    showTransferMessage(action.successMessage)
                },
                onFailure = { error ->
                    if (error is DriveAuthorizationRequiredException) {
                        pendingDriveAction = action
                        val request = DriveAuthorizationRequest(
                            id = nextRequestId(),
                            pendingIntent = error.pendingIntent,
                        )

                        localState.update {
                            it.copy(
                                driveAuthorizationRequest = request,
                            )
                        }
                    } else {
                        showTransferMessage(action.failureMessage)
                    }
                },
            )
        }
    }

    private fun showTransferMessage(message: TransferMessage) {
        val userMessage = UserMessage(
            id = nextRequestId(),
            kind = UserMessageKind.Transfer(message),
        )

        localState.update {
            it.copy(
                userMessage = userMessage,
            )
        }
    }

    private fun nextRequestId(): Long {
        return nextRequestId++
    }

    private companion object {
        const val EXPORT_FILE_NAME = "url-opener-data.json"
        const val INITIAL_REQUEST_ID = 1L
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}

private enum class DriveAction(
    val successMessage: TransferMessage,
    val failureMessage: TransferMessage,
) {
    Export(
        successMessage = TransferMessage.DriveSyncCompleted,
        failureMessage = TransferMessage.DriveSyncFailed,
    ),
    Import(
        successMessage = TransferMessage.DriveImportCompleted,
        failureMessage = TransferMessage.DriveImportFailed,
    ),
}
