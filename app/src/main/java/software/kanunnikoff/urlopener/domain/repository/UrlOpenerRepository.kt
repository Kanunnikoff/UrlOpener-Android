package software.kanunnikoff.urlopener.domain.repository

interface UrlOpenerRepository {
    suspend fun openUrl(url: String): Result<Unit>
}
