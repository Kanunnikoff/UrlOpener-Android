package software.kanunnikoff.urlopener.domain.model

data class AppSettings(
    val shouldAskDeleteConfirmation: Boolean = true,
    val shouldAskOpenConfirmation: Boolean = false,
)
