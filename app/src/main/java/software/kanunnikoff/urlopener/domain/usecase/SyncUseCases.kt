package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.SyncRepository

/**
 * Uploads a fresh data snapshot to Google Drive app data storage.
 */
class ExportBackupUseCase @Inject constructor(
    private val syncRepository: SyncRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        return syncRepository.exportToDrive()
    }
}

/**
 * Restores data from Google Drive app data storage.
 */
class ImportBackupUseCase @Inject constructor(
    private val syncRepository: SyncRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        return syncRepository.importFromDrive()
    }
}
