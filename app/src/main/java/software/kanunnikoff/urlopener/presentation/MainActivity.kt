package software.kanunnikoff.urlopener.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import software.kanunnikoff.urlopener.BuildConfig
import software.kanunnikoff.urlopener.data.AndroidLinkGroupsRepository
import software.kanunnikoff.urlopener.data.AndroidSettingsRepository
import software.kanunnikoff.urlopener.data.AndroidUrlOpenerRepository
import software.kanunnikoff.urlopener.data.db.UrlOpenerDatabase
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
import software.kanunnikoff.urlopener.presentation.theme.UrlOpenerTheme
import software.kanunnikoff.urlopener.presentation.ui.UrlOpenerRoute

class MainActivity : ComponentActivity() {

    private val viewModel: UrlOpenerViewModel by viewModels {
        val settingsRepository = AndroidSettingsRepository(applicationContext)
        val database = Room.databaseBuilder(
            applicationContext,
            UrlOpenerDatabase::class.java,
            "url_opener.db",
        ).build()
        val linkGroupsRepository = AndroidLinkGroupsRepository(database.linkGroupsDao())
        UrlOpenerViewModelFactory(
            openUrlUseCase = OpenUrlUseCase(
                repository = AndroidUrlOpenerRepository(applicationContext),
            ),
            observeSettingsUseCase = ObserveSettingsUseCase(settingsRepository),
            setDeleteConfirmationUseCase = SetDeleteConfirmationUseCase(settingsRepository),
            setOpenConfirmationUseCase = SetOpenConfirmationUseCase(settingsRepository),
            observeLinkGroupsUseCase = ObserveLinkGroupsUseCase(linkGroupsRepository),
            addLinkGroupUseCase = AddLinkGroupUseCase(linkGroupsRepository),
            updateLinkGroupUseCase = UpdateLinkGroupUseCase(linkGroupsRepository),
            deleteLinkGroupUseCase = DeleteLinkGroupUseCase(linkGroupsRepository),
            addSavedLinkUseCase = AddSavedLinkUseCase(linkGroupsRepository),
            updateSavedLinkUseCase = UpdateSavedLinkUseCase(linkGroupsRepository),
            deleteSavedLinkUseCase = DeleteSavedLinkUseCase(linkGroupsRepository),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    onAddGroupClick = viewModel::onAddGroupClick,
                    onEditGroupClick = viewModel::onEditGroupClick,
                    onRequestDeleteGroup = viewModel::onRequestDeleteGroup,
                    onSaveGroup = viewModel::onSaveGroup,
                    onDismissGroupEditor = viewModel::onDismissGroupEditor,
                    onAddLinkClick = viewModel::onAddLinkClick,
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
