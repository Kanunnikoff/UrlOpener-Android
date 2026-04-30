package software.kanunnikoff.urlopener.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import software.kanunnikoff.urlopener.domain.usecase.ObserveSettingsUseCase
import software.kanunnikoff.urlopener.domain.usecase.OpenUrlUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetDeleteConfirmationUseCase

class UrlOpenerViewModelFactory(
    private val openUrlUseCase: OpenUrlUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val setDeleteConfirmationUseCase: SetDeleteConfirmationUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UrlOpenerViewModel(
            openUrlUseCase = openUrlUseCase,
            observeSettingsUseCase = observeSettingsUseCase,
            setDeleteConfirmationUseCase = setDeleteConfirmationUseCase,
        ) as T
    }
}
