package software.kanunnikoff.urlopener.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.kanunnikoff.urlopener.domain.repository.UrlOpenerRepository

class AndroidUrlOpenerRepository(
    private val context: Context,
) : UrlOpenerRepository {

    override suspend fun openUrl(url: String): Result<Unit> = runCatching {
        withContext(Dispatchers.Main.immediate) {
            // Контекст приложения живёт дольше Activity, поэтому не удерживает экран в памяти.
            // Из-за этого для запуска внешнего экрана нужен флаг новой задачи.
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }
}
