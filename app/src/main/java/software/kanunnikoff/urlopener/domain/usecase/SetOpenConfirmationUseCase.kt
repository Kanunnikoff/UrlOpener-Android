package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.SettingsRepository

class SetOpenConfirmationUseCase(
    private val repository: SettingsRepository,
) {

    suspend operator fun invoke(shouldAsk: Boolean) {
        repository.setShouldAskOpenConfirmation(shouldAsk)
    }
}
