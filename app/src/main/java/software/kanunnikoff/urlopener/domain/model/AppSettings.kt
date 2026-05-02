package software.kanunnikoff.urlopener.domain.model

/**
 * User preferences that affect potentially destructive or external actions.
 *
 * Defaults are defined in the domain model so a fresh install behaves consistently even before
 * DataStore has persisted any values.
 */
data class AppSettings(
    val shouldAskDeleteConfirmation: Boolean = true,
    val shouldAskOpenConfirmation: Boolean = false,
)
