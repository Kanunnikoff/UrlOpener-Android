package software.kanunnikoff.urlopener.presentation

import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink

/**
 * Complete immutable state for the main screen.
 *
 * Nullable editor and target fields represent currently visible dialogs. Keeping them in the same
 * state object makes dialog restoration and event handling predictable across recompositions.
 */
data class UrlOpenerState(
    val url: String = "",
    val selectedTab: UrlOpenerTab = UrlOpenerTab.Home,
    val shouldAskDeleteConfirmation: Boolean = true,
    val shouldAskOpenConfirmation: Boolean = false,
    val groups: List<LinkGroup> = emptyList(),
    val groupEditor: GroupEditorState? = null,
    val linkEditor: LinkEditorState? = null,
    val shouldShowGroupPicker: Boolean = false,
    val deleteTarget: DeleteTarget? = null,
    val openTarget: OpenTarget? = null,
)

/**
 * Draft values for creating or editing a group.
 *
 * A null [groupId] means the dialog is creating a new group; a non-null value means it is editing
 * an existing database record.
 */
data class GroupEditorState(
    val groupId: Long? = null,
    val name: String = "",
    val description: String = "",
)

/**
 * Draft values for creating or editing a saved link.
 *
 * [groupId] is always required because every link belongs to exactly one group.
 */
data class LinkEditorState(
    val groupId: Long,
    val linkId: Long? = null,
    val name: String = "",
    val url: String = "",
)

/**
 * Describes the item waiting for deletion confirmation.
 */
sealed interface DeleteTarget {
    data class Group(val groupId: Long) : DeleteTarget
    data class Link(val groupId: Long, val linkId: Long) : DeleteTarget
}

/**
 * Describes the saved link waiting for open confirmation.
 */
data class OpenTarget(
    val link: SavedLink,
)
