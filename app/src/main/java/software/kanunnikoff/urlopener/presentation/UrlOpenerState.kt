package software.kanunnikoff.urlopener.presentation

import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink

data class UrlOpenerState(
    val url: String = "",
    val selectedTab: UrlOpenerTab = UrlOpenerTab.Home,
    val shouldAskDeleteConfirmation: Boolean = true,
    val shouldAskOpenConfirmation: Boolean = false,
    val groups: List<LinkGroup> = emptyList(),
    val groupEditor: GroupEditorState? = null,
    val linkEditor: LinkEditorState? = null,
    val deleteTarget: DeleteTarget? = null,
    val openTarget: OpenTarget? = null,
)

data class GroupEditorState(
    val groupId: Long? = null,
    val name: String = "",
    val description: String = "",
)

data class LinkEditorState(
    val groupId: Long,
    val linkId: Long? = null,
    val name: String = "",
    val url: String = "",
)

sealed interface DeleteTarget {
    data class Group(val groupId: Long) : DeleteTarget
    data class Link(val groupId: Long, val linkId: Long) : DeleteTarget
}

data class OpenTarget(
    val link: SavedLink,
)
