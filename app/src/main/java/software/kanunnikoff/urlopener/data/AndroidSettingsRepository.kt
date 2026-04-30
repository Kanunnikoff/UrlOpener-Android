package software.kanunnikoff.urlopener.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import software.kanunnikoff.urlopener.domain.model.AppSettings
import software.kanunnikoff.urlopener.domain.repository.SettingsRepository

private val Context.settingsDataStore by preferencesDataStore(
    name = "url_opener_settings",
)

class AndroidSettingsRepository(
    private val context: Context,
) : SettingsRepository {

    override val settings: Flow<AppSettings> = context.settingsDataStore.data
        .map { preferences ->
            AppSettings(
                shouldAskDeleteConfirmation = preferences[SHOULD_ASK_DELETE_CONFIRMATION_KEY]
                    ?: true,
                shouldAskOpenConfirmation = preferences[SHOULD_ASK_OPEN_CONFIRMATION_KEY]
                    ?: false,
            )
        }

    override suspend fun setShouldAskDeleteConfirmation(shouldAsk: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOULD_ASK_DELETE_CONFIRMATION_KEY] = shouldAsk
        }
    }

    override suspend fun setShouldAskOpenConfirmation(shouldAsk: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOULD_ASK_OPEN_CONFIRMATION_KEY] = shouldAsk
        }
    }

    private companion object {
        val SHOULD_ASK_DELETE_CONFIRMATION_KEY = booleanPreferencesKey(
            name = "should_ask_delete_confirmation",
        )
        val SHOULD_ASK_OPEN_CONFIRMATION_KEY = booleanPreferencesKey(
            name = "should_ask_open_confirmation",
        )
    }
}
