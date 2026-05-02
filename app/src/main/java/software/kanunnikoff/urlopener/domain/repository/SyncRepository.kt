package software.kanunnikoff.urlopener.domain.repository

/**
 * Saves and restores a data snapshot through Android system backup.
 */
interface SyncRepository {

    suspend fun exportToDrive(): Result<Unit>

    suspend fun importFromDrive(): Result<Unit>
}
