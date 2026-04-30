package software.kanunnikoff.urlopener.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.kanunnikoff.urlopener.domain.usecase.ObserveSettingsUseCase
import software.kanunnikoff.urlopener.domain.usecase.OpenUrlUseCase
import software.kanunnikoff.urlopener.domain.usecase.SetDeleteConfirmationUseCase
import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab

class UrlOpenerViewModel(
    private val openUrlUseCase: OpenUrlUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val setDeleteConfirmationUseCase: SetDeleteConfirmationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(UrlOpenerState())
    val state: StateFlow<UrlOpenerState> = _state

    private val events = Channel<UrlOpenerEvent>(Channel.BUFFERED)
    val eventFlow = events.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeSettingsUseCase().collect { settings ->
                _state.update {
                    it.copy(shouldAskDeleteConfirmation = settings.shouldAskDeleteConfirmation)
                }
            }
        }
    }

    fun onTabSelected(tab: UrlOpenerTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onUrlChanged(url: String) {
        _state.update { it.copy(url = url) }
    }

    fun onClearClick() {
        _state.update { it.copy(url = "") }
    }

    fun onOpenClick() {
        viewModelScope.launch {
            val result = openUrlUseCase(_state.value.url)

            result.exceptionOrNull()?.let { exception ->
                // Ошибка открытия ссылки является одноразовым сообщением.
                // Если положить её в состояние экрана, она могла бы повториться после пересоздания Activity.
                events.send(UrlOpenerEvent.ShowError(exception.localizedMessage.orEmpty()))
            }
        }
    }

    fun onDeleteConfirmationChanged(shouldAsk: Boolean) {
        viewModelScope.launch {
            setDeleteConfirmationUseCase(shouldAsk)
        }
    }
}
