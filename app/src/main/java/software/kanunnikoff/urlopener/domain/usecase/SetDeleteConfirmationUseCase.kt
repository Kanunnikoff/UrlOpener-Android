package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.SettingsRepository

/**
 * Persists whether destructive actions should ask for confirmation.
 */
class SetDeleteConfirmationUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {

    suspend operator fun invoke(shouldAsk: Boolean) {
        repository.setShouldAskDeleteConfirmation(shouldAsk)
    }
}
