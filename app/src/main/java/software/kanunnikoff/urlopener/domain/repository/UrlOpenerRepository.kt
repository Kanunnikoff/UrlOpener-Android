package software.kanunnikoff.urlopener.domain.repository

/**
 * Boundary for launching a URL outside the app.
 *
 * Returning [Result] keeps platform failures explicit for the ViewModel while avoiding Android
 * dependencies in domain code.
 */
interface UrlOpenerRepository {
    suspend fun openUrl(url: String): Result<Unit>
}
