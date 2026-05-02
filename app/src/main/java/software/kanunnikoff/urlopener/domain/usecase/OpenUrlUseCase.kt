package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.UrlOpenerRepository

/**
 * Normalizes the entered URL and delegates opening to the platform repository.
 */
class OpenUrlUseCase @Inject constructor(
    private val repository: UrlOpenerRepository,
) {

    suspend operator fun invoke(url: String): Result<Unit> {
        return repository.openUrl(url.trim())
    }
}
