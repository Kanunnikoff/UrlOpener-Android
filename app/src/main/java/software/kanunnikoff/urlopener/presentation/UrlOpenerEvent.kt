package software.kanunnikoff.urlopener.presentation

/**
 * One-time messages that the screen handles outside persistent state.
 */
sealed interface UrlOpenerEvent {
    data class ShowError(val message: String) : UrlOpenerEvent
    data object OpenUrlFailed : UrlOpenerEvent
    data class ExportJsonRequested(val fileName: String, val json: String) : UrlOpenerEvent
    data object ImportJsonRequested : UrlOpenerEvent
    data class ShowTransferMessage(val message: TransferMessage) : UrlOpenerEvent
}

enum class TransferMessage {
    ExportCompleted,
    ExportFailed,
    ImportCompleted,
    ImportFailed,
    DriveSyncCompleted,
    DriveSyncFailed,
    DriveImportCompleted,
    DriveImportFailed,
}
