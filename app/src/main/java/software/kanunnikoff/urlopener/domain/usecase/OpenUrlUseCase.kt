package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.UrlOpenerRepository

class OpenUrlUseCase(
    private val repository: UrlOpenerRepository,
) {

    suspend operator fun invoke(url: String): Result<Unit> {
        return repository.openUrl(url.trim())
    }
}
