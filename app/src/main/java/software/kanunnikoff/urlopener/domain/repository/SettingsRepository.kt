package software.kanunnikoff.urlopener.domain.repository

import kotlinx.coroutines.flow.Flow
import software.kanunnikoff.urlopener.domain.model.AppSettings

interface SettingsRepository {
    val settings: Flow<AppSettings>

    suspend fun setShouldAskDeleteConfirmation(shouldAsk: Boolean)
}
