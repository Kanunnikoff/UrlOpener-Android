package software.kanunnikoff.urlopener.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import software.kanunnikoff.urlopener.data.AndroidUrlOpenerRepository
import software.kanunnikoff.urlopener.domain.usecase.OpenUrlUseCase
import software.kanunnikoff.urlopener.presentation.theme.UrlOpenerTheme
import software.kanunnikoff.urlopener.presentation.ui.UrlOpenerRoute

class MainActivity : ComponentActivity() {

    private val viewModel: UrlOpenerViewModel by viewModels {
        UrlOpenerViewModelFactory(
            openUrlUseCase = OpenUrlUseCase(
                repository = AndroidUrlOpenerRepository(applicationContext),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UrlOpenerTheme {
                UrlOpenerRoute(
                    state = viewModel.state.collectAsStateWithLifecycle().value,
                    events = viewModel.eventFlow,
                    onUrlChanged = viewModel::onUrlChanged,
                    onClearClick = viewModel::onClearClick,
                    onOpenClick = viewModel::onOpenClick,
                )
            }
        }
    }
}
