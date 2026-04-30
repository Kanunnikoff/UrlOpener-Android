package software.kanunnikoff.urlopener.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.domain.usecase.AddLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.AddSavedLinkUseCase
import software.kanunnikoff.urlopener.domain.usecase.DeleteLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.DeleteSavedLinkUseCase
import software.kanunnikoff.urlopener.domain.usecase.ObserveLinkGroupsUseCase
import software.kanunnikoff.urlopener.domain.usecase.ObserveSettingsUseCase
import software.kanunnikoff.urlopener.domain.usecase.OpenUrlUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetDeleteConfirmationUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetOpenConfirmationUseCase
import software.kanunnikoff.urlopener.domain.usecase.UpdateLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.UpdateSavedLinkUseCase
import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab

class UrlOpenerViewModel(
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
) : ViewModel() {

    private val _state = MutableStateFlow(UrlOpenerState())
    val state: StateFlow<UrlOpenerState> = _state

    private val events = Channel<UrlOpenerEvent>(Channel.BUFFERED)
    val eventFlow = events.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeSettingsUseCase().collect { settings ->
                _state.update {
                    it.copy(
                        shouldAskDeleteConfirmation = settings.shouldAskDeleteConfirmation,
                        shouldAskOpenConfirmation = settings.shouldAskOpenConfirmation,
                    )
                }
            }
        }
        viewModelScope.launch {
            observeLinkGroupsUseCase().collect { groups ->
                _state.update { state ->
                    state.copy(groups = groups)
                }
            }
        }
    }

    fun onTabSelected(tab: UrlOpenerTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onUrlChanged(url: String) {
        _state.update { it.copy(url = url) }
    }

    fun onClearClick() {
        _state.update { it.copy(url = "") }
    }

    fun onOpenClick() {
        openUrl(_state.value.url)
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

    fun onAddGroupClick() {
        _state.update { it.copy(groupEditor = GroupEditorState()) }
    }

    fun onEditGroupClick(group: LinkGroup) {
        _state.update {
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
        _state.update { it.copy(groupEditor = null) }
    }

    fun onSaveGroup(name: String, description: String) {
        val editor = _state.value.groupEditor
        viewModelScope.launch {
            if (editor?.groupId == null) {
                addLinkGroupUseCase(name, description)
            } else {
                updateLinkGroupUseCase(editor.groupId, name, description)
            }
            _state.update { it.copy(groupEditor = null) }
        }
    }

    fun onRequestDeleteGroup(groupId: Long) {
        if (_state.value.shouldAskDeleteConfirmation) {
            _state.update { it.copy(deleteTarget = DeleteTarget.Group(groupId)) }
        } else {
            deleteGroup(groupId)
        }
    }

    fun onAddLinkClick(groupId: Long) {
        _state.update { it.copy(linkEditor = LinkEditorState(groupId = groupId, url = it.url)) }
    }

    fun onSaveEnteredLinkClick() {
        val groups = _state.value.groups
        when (groups.size) {
            0 -> _state.update { it.copy(groupEditor = GroupEditorState()) }
            1 -> onAddLinkClick(groups.first().id)
            else -> _state.update { it.copy(shouldShowGroupPicker = true) }
        }
    }

    fun onGroupPickedForEnteredLink(groupId: Long) {
        _state.update {
            it.copy(
                shouldShowGroupPicker = false,
                linkEditor = LinkEditorState(groupId = groupId, url = it.url),
            )
        }
    }

    fun onDismissGroupPicker() {
        _state.update { it.copy(shouldShowGroupPicker = false) }
    }

    fun onEditLinkClick(groupId: Long, link: SavedLink) {
        _state.update {
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
        _state.update { it.copy(linkEditor = null) }
    }

    fun onSaveLink(groupId: Long, name: String, url: String) {
        val editor = _state.value.linkEditor
        viewModelScope.launch {
            if (editor?.linkId == null) {
                addSavedLinkUseCase(groupId, name, url)
            } else {
                updateSavedLinkUseCase(groupId, editor.linkId, name, url)
            }
            _state.update { it.copy(linkEditor = null) }
        }
    }

    fun onRequestDeleteLink(groupId: Long, linkId: Long) {
        if (_state.value.shouldAskDeleteConfirmation) {
            _state.update { it.copy(deleteTarget = DeleteTarget.Link(groupId, linkId)) }
        } else {
            deleteLink(groupId, linkId)
        }
    }

    fun onDismissDeleteConfirmation() {
        _state.update { it.copy(deleteTarget = null) }
    }

    fun onConfirmDelete() {
        when (val target = _state.value.deleteTarget) {
            is DeleteTarget.Group -> deleteGroup(target.groupId)
            is DeleteTarget.Link -> deleteLink(target.groupId, target.linkId)
            null -> Unit
        }
    }

    fun onSavedLinkClick(groupId: Long, linkId: Long) {
        val link = _state.value.groups
            .firstOrNull { it.id == groupId }
            ?.links
            ?.firstOrNull { it.id == linkId }
            ?: return

        if (_state.value.shouldAskOpenConfirmation) {
            _state.update { it.copy(openTarget = OpenTarget(link)) }
        } else {
            openUrl(link.url)
        }
    }

    fun onDismissOpenConfirmation() {
        _state.update { it.copy(openTarget = null) }
    }

    fun onConfirmOpenSavedLink() {
        val link = _state.value.openTarget?.link ?: return
        _state.update { it.copy(openTarget = null) }
        openUrl(link.url)
    }

    private fun deleteGroup(groupId: Long) {
        viewModelScope.launch {
            deleteLinkGroupUseCase(groupId)
            _state.update { it.copy(deleteTarget = null) }
        }
    }

    private fun deleteLink(groupId: Long, linkId: Long) {
        viewModelScope.launch {
            deleteSavedLinkUseCase(groupId, linkId)
            _state.update { it.copy(deleteTarget = null) }
        }
    }

    private fun openUrl(url: String) {
        viewModelScope.launch {
            val result = openUrlUseCase(url)

            if (result.isFailure) {
                // Ошибка открытия ссылки является одноразовым сообщением.
                // Если положить её в состояние экрана, она могла бы повториться после пересоздания Activity.
                events.send(UrlOpenerEvent.OpenUrlFailed)
            }
        }
    }
}
