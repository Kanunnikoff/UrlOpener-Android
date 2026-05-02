package software.kanunnikoff.urlopener.data

import android.content.Context
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File as DriveFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.repository.DriveAuthorizationRequiredException
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository
import software.kanunnikoff.urlopener.domain.repository.SyncRepository
import software.kanunnikoff.urlopener.domain.service.LinkGroupsJsonCodec
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject

/**
 * Synchronizes a portable data snapshot with the app-private Google Drive folder.
 */
class GoogleDriveSyncRepository @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
    private val linkGroupsRepository: LinkGroupsRepository,
    private val codec: LinkGroupsJsonCodec,
) : SyncRepository {

    override suspend fun exportToDrive(): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            val drive = driveService()
            val content = ByteArrayContent(
                JSON_MIME_TYPE,
                codec.encode(linkGroupsRepository.groups.first()).toByteArray(StandardCharsets.UTF_8),
            )

            val fileId = findBackupFileId(drive)

            if (fileId == null) {
                drive.files()
                    .create(
                        DriveFile()
                            .setName(BACKUP_FILE_NAME)
                            .setMimeType(JSON_MIME_TYPE)
                            .setParents(listOf(APP_DATA_FOLDER)),
                        content,
                    )
                    .setFields(FILE_ID_FIELD)
                    .execute()
            } else {
                drive.files()
                    .update(fileId, null, content)
                    .execute()
            }
        }
    }

    override suspend fun importFromDrive(): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            val drive = driveService()
            val fileId = findBackupFileId(drive) ?: error(
                context.getString(R.string.backup_missing),
            )

            val output = ByteArrayOutputStream()
            drive.files()
                .get(fileId)
                .executeMediaAndDownloadTo(output)

            linkGroupsRepository.replaceGroups(
                codec.decode(output.toString(StandardCharsets.UTF_8.name())),
            )
        }
    }

    private suspend fun driveService(): Drive {
        val authorization = Identity.getAuthorizationClient(context)
            .authorize(authorizationRequest())
            .await()

        if (authorization.hasResolution()) {
            throw DriveAuthorizationRequiredException(
                authorization.pendingIntent ?: error(context.getString(R.string.drive_auth_resolution_missing)),
            )
        }

        val accessToken = authorization.accessToken ?: error(
            context.getString(R.string.drive_auth_token_missing),
        )

        return Drive.Builder(
            NetHttpTransport.Builder().build(),
            GsonFactory.getDefaultInstance(),
            HttpRequestInitializer { request ->
                request.headers.authorization = "$AUTHORIZATION_TYPE $accessToken"
            },
        )
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }

    private fun authorizationRequest(): AuthorizationRequest {
        return AuthorizationRequest.builder()
            .setRequestedScopes(listOf(Scope(DriveScopes.DRIVE_APPDATA)))
            .build()
    }

    private fun findBackupFileId(drive: Drive): String? {
        return drive.files()
            .list()
            .setSpaces(APP_DATA_FOLDER)
            .setQ("name = '$BACKUP_FILE_NAME' and trashed = false")
            .setFields("files($FILE_ID_FIELD)")
            .setPageSize(1)
            .execute()
            .files
            .firstOrNull()
            ?.id
    }

    private companion object {
        const val APP_DATA_FOLDER = "appDataFolder"
        const val AUTHORIZATION_TYPE = "Bearer"
        const val BACKUP_FILE_NAME = "url_opener_backup.json"
        const val FILE_ID_FIELD = "id"
        const val JSON_MIME_TYPE = "application/json"
    }
}
