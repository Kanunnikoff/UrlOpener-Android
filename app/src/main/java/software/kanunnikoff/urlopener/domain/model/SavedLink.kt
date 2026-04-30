package software.kanunnikoff.urlopener.domain.model

data class SavedLink(
    val id: Long = 0L,
    val name: String,
    val url: String,
)
