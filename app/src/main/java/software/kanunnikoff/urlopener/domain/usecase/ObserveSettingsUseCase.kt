package software.kanunnikoff.urlopener.domain.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.model.AppSettings
import software.kanunnikoff.urlopener.domain.repository.SettingsRepository

/**
 * Observes persisted user settings.
 */
class ObserveSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {

    operator fun invoke(): Flow<AppSettings> {
        return repository.settings
    }
}
