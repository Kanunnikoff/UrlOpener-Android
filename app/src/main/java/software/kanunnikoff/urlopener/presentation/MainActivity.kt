package software.kanunnikoff.urlopener.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import software.kanunnikoff.urlopener.BuildConfig
import software.kanunnikoff.urlopener.data.AndroidSettingsRepository
import software.kanunnikoff.urlopener.data.AndroidUrlOpenerRepository
import software.kanunnikoff.urlopener.domain.usecase.ObserveSettingsUseCase
import software.kanunnikoff.urlopener.domain.usecase.OpenUrlUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetDeleteConfirmationUseCase
import software.kanunnikoff.urlopener.presentation.theme.UrlOpenerTheme
import software.kanunnikoff.urlopener.presentation.ui.UrlOpenerRoute

class MainActivity : ComponentActivity() {

    private val viewModel: UrlOpenerViewModel by viewModels {
        val settingsRepository = AndroidSettingsRepository(applicationContext)
        UrlOpenerViewModelFactory(
            openUrlUseCase = OpenUrlUseCase(
                repository = AndroidUrlOpenerRepository(applicationContext),
            ),
            observeSettingsUseCase = ObserveSettingsUseCase(settingsRepository),
            setDeleteConfirmationUseCase = SetDeleteConfirmationUseCase(settingsRepository),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UrlOpenerTheme {
                UrlOpenerRoute(
                    state = viewModel.state.collectAsStateWithLifecycle().value,
                    events = viewModel.eventFlow,
                    appVersionName = BuildConfig.VERSION_NAME,
                    appVersionCode = BuildConfig.VERSION_CODE,
                    onTabSelected = viewModel::onTabSelected,
                    onUrlChanged = viewModel::onUrlChanged,
                    onClearClick = viewModel::onClearClick,
                    onOpenClick = viewModel::onOpenClick,
                    onDeleteConfirmationChanged = viewModel::onDeleteConfirmationChanged,
                )
            }
        }
    }
}
