package software.kanunnikoff.urlopener.presentation

sealed interface UrlOpenerEvent {
    data class ShowError(val message: String) : UrlOpenerEvent
}
