package software.kanunnikoff.urlopener.domain.usecase

import kotlinx.coroutines.flow.Flow
import software.kanunnikoff.urlopener.domain.model.AppSettings
import software.kanunnikoff.urlopener.domain.repository.SettingsRepository

class ObserveSettingsUseCase(
    private val repository: SettingsRepository,
) {

    operator fun invoke(): Flow<AppSettings> {
        return repository.settings
    }
}
