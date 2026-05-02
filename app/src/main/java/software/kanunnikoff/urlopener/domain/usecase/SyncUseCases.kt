package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.SyncRepository

/**
 * Marks a fresh data snapshot for system sync with Google Drive.
 */
class ExportBackupUseCase @Inject constructor(
    private val syncRepository: SyncRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        return syncRepository.exportToDrive()
    }
}

/**
 * Restores data from the local snapshot that Android receives through system backup.
 */
class ImportBackupUseCase @Inject constructor(
    private val syncRepository: SyncRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        return syncRepository.importFromDrive()
    }
}
