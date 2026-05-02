package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.SettingsRepository

/**
 * Persists whether destructive actions should ask for confirmation.
 */
class SetDeleteConfirmationUseCase(
    private val repository: SettingsRepository,
) {

    suspend operator fun invoke(shouldAsk: Boolean) {
        repository.setShouldAskDeleteConfirmation(shouldAsk)
    }
}
