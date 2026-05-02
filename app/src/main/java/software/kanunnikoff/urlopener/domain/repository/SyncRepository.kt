package software.kanunnikoff.urlopener.domain.repository

import android.app.PendingIntent

/**
 * Saves and restores a data snapshot through the app-private Google Drive storage.
 */
interface SyncRepository {

    suspend fun exportToDrive(): Result<Unit>

    suspend fun importFromDrive(): Result<Unit>
}

class DriveAuthorizationRequiredException(
    val pendingIntent: PendingIntent,
) : IllegalStateException()
