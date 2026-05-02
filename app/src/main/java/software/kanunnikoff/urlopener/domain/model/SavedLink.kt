package software.kanunnikoff.urlopener.domain.model

/**
 * A user-saved link with a display name and the URL that will be passed to Android for opening.
 */
data class SavedLink(
    val id: Long = 0L,
    val name: String,
    val url: String,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
