package software.kanunnikoff.urlopener.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import software.kanunnikoff.urlopener.domain.usecase.OpenUrlUseCase

class UrlOpenerViewModelFactory(
    private val openUrlUseCase: OpenUrlUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UrlOpenerViewModel(openUrlUseCase) as T
    }
}
