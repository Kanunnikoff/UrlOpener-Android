package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.SettingsRepository

/**
 * Persists whether opening a saved link should ask for confirmation.
 */
class SetOpenConfirmationUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {

    suspend operator fun invoke(shouldAsk: Boolean) {
        repository.setShouldAskOpenConfirmation(shouldAsk)
    }
}
