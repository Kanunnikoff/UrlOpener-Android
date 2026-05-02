package software.kanunnikoff.urlopener

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import software.kanunnikoff.urlopener.domain.model.AppSettings
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository
import software.kanunnikoff.urlopener.domain.repository.SettingsRepository
import software.kanunnikoff.urlopener.domain.repository.SyncRepository
import software.kanunnikoff.urlopener.domain.repository.UrlOpenerRepository
import software.kanunnikoff.urlopener.domain.service.LinkGroupsJsonCodec
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
import software.kanunnikoff.urlopener.presentation.UrlOpenerViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class UrlOpenerViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var groupsRepository: FakeLinkGroupsRepository
    private lateinit var settingsRepository: FakeSettingsRepository
    private lateinit var syncRepository: FakeSyncRepository
    private lateinit var urlOpenerRepository: FakeUrlOpenerRepository
    private lateinit var viewModel: UrlOpenerViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        groupsRepository = FakeLinkGroupsRepository()
        settingsRepository = FakeSettingsRepository()
        syncRepository = FakeSyncRepository()
        urlOpenerRepository = FakeUrlOpenerRepository()
        val jsonCodec = LinkGroupsJsonCodec()

        viewModel = UrlOpenerViewModel(
            openUrlUseCase = OpenUrlUseCase(urlOpenerRepository),
            observeSettingsUseCase = ObserveSettingsUseCase(settingsRepository),
            setDeleteConfirmationUseCase = SetDeleteConfirmationUseCase(settingsRepository),
            setOpenConfirmationUseCase = SetOpenConfirmationUseCase(settingsRepository),
            observeLinkGroupsUseCase = ObserveLinkGroupsUseCase(groupsRepository),
            addLinkGroupUseCase = AddLinkGroupUseCase(groupsRepository),
            updateLinkGroupUseCase = UpdateLinkGroupUseCase(groupsRepository),
            deleteLinkGroupUseCase = DeleteLinkGroupUseCase(groupsRepository),
            addSavedLinkUseCase = AddSavedLinkUseCase(groupsRepository),
            updateSavedLinkUseCase = UpdateSavedLinkUseCase(groupsRepository),
            deleteSavedLinkUseCase = DeleteSavedLinkUseCase(groupsRepository),
            exportLinkGroupsJsonUseCase = ExportLinkGroupsJsonUseCase(jsonCodec),
            importLinkGroupsJsonUseCase = ImportLinkGroupsJsonUseCase(groupsRepository, jsonCodec),
            exportBackupUseCase = ExportBackupUseCase(syncRepository),
            importBackupUseCase = ImportBackupUseCase(syncRepository),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addGroupAndLinkShowsLinkInsideGroup() = runTest(dispatcher) {
        val uiStateCollection = collectUiState(this)

        viewModel.onSaveGroup(name = "Работа", description = "Служебные ссылки")
        viewModel.onSaveLink(groupId = 1L, name = "Почта", url = "https://mail.example")
        advanceUntilIdle()

        assertEquals("Работа", viewModel.uiState.value.groups.single().name)
        assertEquals("Почта", viewModel.uiState.value.groups.single().links.single().name)

        uiStateCollection.cancel()
    }

    @Test
    fun openConfirmationFlagDelaysOpeningSavedLink() = runTest(dispatcher) {
        val uiStateCollection = collectUiState(this)

        settingsRepository.setShouldAskOpenConfirmation(true)
        viewModel.onSaveGroup(name = "Работа", description = "")
        viewModel.onSaveLink(groupId = 1L, name = "Почта", url = "https://mail.example")
        advanceUntilIdle()

        viewModel.onSavedLinkClick(groupId = 1L, linkId = 1L)
        advanceUntilIdle()
        assertTrue(urlOpenerRepository.openedUrls.isEmpty())

        viewModel.onConfirmOpenSavedLink()
        advanceUntilIdle()
        assertEquals(listOf("https://mail.example"), urlOpenerRepository.openedUrls)

        uiStateCollection.cancel()
    }

    private fun collectUiState(scope: TestScope): Job {
        return scope.launch {
            viewModel.uiState.collect {}
        }
    }

    private class FakeLinkGroupsRepository : LinkGroupsRepository {
        override val groups = MutableStateFlow<List<LinkGroup>>(emptyList())
        private var groupCounter = 0L
        private var linkCounter = 0L

        override suspend fun addGroup(name: String, description: String) {
            groupCounter += 1
            groups.value = groups.value + LinkGroup(
                id = groupCounter,
                name = name,
                description = description,
            )
        }

        override suspend fun updateGroup(groupId: Long, name: String, description: String) {
            groups.value = groups.value.map {
                if (it.id == groupId) it.copy(name = name, description = description) else it
            }
        }

        override suspend fun deleteGroup(groupId: Long) {
            groups.value = groups.value.filterNot { it.id == groupId }
        }

        override suspend fun addLink(groupId: Long, name: String, url: String) {
            linkCounter += 1
            groups.value = groups.value.map {
                if (it.id == groupId) {
                    it.copy(
                        links = it.links + SavedLink(
                            id = linkCounter,
                            name = name,
                            url = url,
                        ),
                    )
                } else {
                    it
                }
            }
        }

        override suspend fun updateLink(groupId: Long, linkId: Long, name: String, url: String) = Unit

        override suspend fun deleteLink(groupId: Long, linkId: Long) = Unit

        override suspend fun replaceGroups(groups: List<LinkGroup>) {
            this.groups.value = groups.mapIndexed { index, group ->
                group.copy(id = index + 1L)
            }
        }
    }

    private class FakeSettingsRepository : SettingsRepository {
        override val settings = MutableStateFlow(AppSettings())

        override suspend fun setShouldAskDeleteConfirmation(shouldAsk: Boolean) {
            settings.value = settings.value.copy(shouldAskDeleteConfirmation = shouldAsk)
        }

        override suspend fun setShouldAskOpenConfirmation(shouldAsk: Boolean) {
            settings.value = settings.value.copy(shouldAskOpenConfirmation = shouldAsk)
        }

    }

    private class FakeSyncRepository : SyncRepository {

        override suspend fun exportToDrive(): Result<Unit> {
            return Result.success(Unit)
        }

        override suspend fun importFromDrive(): Result<Unit> {
            return Result.success(Unit)
        }
    }

    private class FakeUrlOpenerRepository : UrlOpenerRepository {
        val openedUrls = mutableListOf<String>()

        override suspend fun openUrl(url: String): Result<Unit> {
            openedUrls += url
            return Result.success(Unit)
        }
    }
}
