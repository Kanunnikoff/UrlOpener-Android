package software.kanunnikoff.urlopener.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import software.kanunnikoff.urlopener.domain.usecase.AddLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.AddSavedLinkUseCase
import software.kanunnikoff.urlopener.domain.usecase.DeleteLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.DeleteSavedLinkUseCase
import software.kanunnikoff.urlopener.domain.usecase.ExportBackupUseCase
import software.kanunnikoff.urlopener.domain.usecase.ExportLinkGroupsJsonUseCase
import software.kanunnikoff.urlopener.domain.usecase.ImportBackupUseCase
import software.kanunnikoff.urlopener.domain.usecase.ImportLinkGroupsJsonUseCase
import software.kanunnikoff.urlopener.domain.usecase.ObserveLinkGroupsUseCase
import software.kanunnikoff.urlopener.domain.usecase.ObserveSettingsUseCase
import software.kanunnikoff.urlopener.domain.usecase.OpenUrlUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetDeleteConfirmationUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetOpenConfirmationUseCase
import software.kanunnikoff.urlopener.domain.usecase.UpdateLinkGroupUseCase
import software.kanunnikoff.urlopener.domain.usecase.UpdateSavedLinkUseCase

/**
 * Creates [UrlOpenerViewModel] with manually wired dependencies.
 *
 * The app is small enough that this keeps startup explicit without introducing a dependency
 * injection framework.
 */
class UrlOpenerViewModelFactory(
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
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UrlOpenerViewModel(
            openUrlUseCase = openUrlUseCase,
            observeSettingsUseCase = observeSettingsUseCase,
            setDeleteConfirmationUseCase = setDeleteConfirmationUseCase,
            setOpenConfirmationUseCase = setOpenConfirmationUseCase,
            observeLinkGroupsUseCase = observeLinkGroupsUseCase,
            addLinkGroupUseCase = addLinkGroupUseCase,
            updateLinkGroupUseCase = updateLinkGroupUseCase,
            deleteLinkGroupUseCase = deleteLinkGroupUseCase,
            addSavedLinkUseCase = addSavedLinkUseCase,
            updateSavedLinkUseCase = updateSavedLinkUseCase,
            deleteSavedLinkUseCase = deleteSavedLinkUseCase,
            exportLinkGroupsJsonUseCase = exportLinkGroupsJsonUseCase,
            importLinkGroupsJsonUseCase = importLinkGroupsJsonUseCase,
            exportBackupUseCase = exportBackupUseCase,
            importBackupUseCase = importBackupUseCase,
        ) as T
    }
}
