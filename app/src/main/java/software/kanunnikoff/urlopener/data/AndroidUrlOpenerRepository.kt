package software.kanunnikoff.urlopener.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.kanunnikoff.urlopener.domain.repository.UrlOpenerRepository
import javax.inject.Inject

/**
 * Opens links through Android's activity resolution system.
 *
 * The repository is kept behind the domain interface so the rest of the app can request URL opening
 * without depending on Android framework classes.
 */
class AndroidUrlOpenerRepository @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
) : UrlOpenerRepository {

    override suspend fun openUrl(url: String): Result<Unit> = runCatching {
        withContext(Dispatchers.Main.immediate) {
            // The application context outlives an Activity and does not keep the screen in memory.
            // Because of that, Android requires a new task flag when starting an external screen.
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(/* uriString = */ url))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }
}
