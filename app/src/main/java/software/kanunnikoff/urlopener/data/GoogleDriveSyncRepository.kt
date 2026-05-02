package software.kanunnikoff.urlopener.data

import android.app.backup.BackupManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository
import software.kanunnikoff.urlopener.domain.repository.SyncRepository
import software.kanunnikoff.urlopener.domain.service.LinkGroupsJsonCodec
import java.io.File
import javax.inject.Inject

/**
 * Synchronizes a portable data snapshot through Android system backup.
 */
class GoogleDriveSyncRepository @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
    private val linkGroupsRepository: LinkGroupsRepository,
    private val codec: LinkGroupsJsonCodec,
) : SyncRepository {

    override suspend fun exportToDrive(): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            backupFile().writeText(codec.encode(linkGroupsRepository.groups.first()))

            // Android uploads this file to Google Drive during the next system backup pass.
            BackupManager(context).dataChanged()
        }
    }

    override suspend fun importFromDrive(): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            val file = backupFile()

            check(file.exists()) {
                context.getString(R.string.backup_missing)
            }

            linkGroupsRepository.replaceGroups(codec.decode(file.readText()))
        }
    }

    private fun backupFile(): File {
        return File(context.filesDir, BACKUP_FILE_NAME)
    }

    private companion object {
        const val BACKUP_FILE_NAME = "url_opener_backup.json"
    }
}
