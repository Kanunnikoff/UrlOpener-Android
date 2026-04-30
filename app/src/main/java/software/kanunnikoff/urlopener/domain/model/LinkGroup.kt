package software.kanunnikoff.urlopener.domain.model

data class LinkGroup(
    val id: Long = 0L,
    val name: String,
    val description: String,
    val links: List<SavedLink> = emptyList(),
)
