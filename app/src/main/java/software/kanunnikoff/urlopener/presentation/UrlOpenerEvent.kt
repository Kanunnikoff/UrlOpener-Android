package software.kanunnikoff.urlopener.presentation

/**
 * One-time messages that the screen handles outside persistent state.
 */
sealed interface UrlOpenerEvent {
    data class ShowError(val message: String) : UrlOpenerEvent
    data object OpenUrlFailed : UrlOpenerEvent
}
