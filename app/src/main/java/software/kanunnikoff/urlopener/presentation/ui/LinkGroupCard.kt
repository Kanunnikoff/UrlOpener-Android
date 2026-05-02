package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import kotlin.math.roundToInt

/**
 * Expandable card that shows one saved-link group and its group-level actions.
 *
 * @param group Group data and child links to display.
 * @param isExpanded Whether the card currently shows its child links.
 * @param onGroupClick Called when the user toggles the expanded state.
 * @param onEditGroupClick Called when the user starts editing the group.
 * @param onRequestDeleteGroup Called when the user requests group deletion.
 * @param onAddLinkClick Called when the user starts adding a link to the group.
 * @param onEditLinkClick Called when the user starts editing a saved link.
 * @param onRequestDeleteLink Called when the user requests saved-link deletion.
 * @param onSavedLinkClick Called when the user selects a saved link for opening.
 */
@Composable
internal fun LinkGroupCard(
    group: LinkGroup,
    isExpanded: Boolean,
    onGroupClick: () -> Unit,
    onEditGroupClick: (LinkGroup) -> Unit,
    onRequestDeleteGroup: (Long) -> Unit,
    onAddLinkClick: (Long) -> Unit,
    onEditLinkClick: (Long, SavedLink) -> Unit,
    onRequestDeleteLink: (Long, Long) -> Unit,
    onSavedLinkClick: (Long, Long) -> Unit,
) {
    val accentColor = FriendlyCardDefaults.groupAccentColor(group.id)

    SwipeActionBox(
        editContentDescription = stringResource(R.string.edit_group),
        deleteContentDescription = stringResource(R.string.delete_group),
        onEdit = { onEditGroupClick(group) },
        onDelete = { onRequestDeleteGroup(group.id) },
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onGroupClick),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .width(6.dp)
                            .height(44.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(accentColor),
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = group.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        if (group.description.isNotBlank()) {
                            Text(
                                text = group.description,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                    Surface(
                        color = accentColor.copy(alpha = 0.16f),
                        contentColor = accentColor,
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Text(
                            text = group.links.size.toString(),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Icon(
                        imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = if (isExpanded) {
                            stringResource(R.string.collapse_group)
                        } else {
                            stringResource(R.string.expand_group)
                        },
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp),
                    )
                    IconButton(onClick = { onAddLinkClick(group.id) }) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(R.string.add_link),
                            tint = accentColor,
                        )
                    }
                }

                if (isExpanded) {
                    HorizontalDivider()
                    if (group.links.isEmpty()) {
                        Text(
                            text = stringResource(R.string.empty_links_message),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    } else {
                        group.links.forEachIndexed { index, link ->
                            SavedLinkRow(
                                groupId = group.id,
                                link = link,
                                onEditLinkClick = onEditLinkClick,
                                onRequestDeleteLink = onRequestDeleteLink,
                                onSavedLinkClick = onSavedLinkClick,
                            )
                            if (index < group.links.lastIndex) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Single saved-link row with open, edit, and delete interactions.
 *
 * @param groupId Identifier of the group that owns the link.
 * @param link Saved-link data to display.
 * @param onEditLinkClick Called when the user starts editing the saved link.
 * @param onRequestDeleteLink Called when the user requests saved-link deletion.
 * @param onSavedLinkClick Called when the user selects the saved link for opening.
 */
@Composable
private fun SavedLinkRow(
    groupId: Long,
    link: SavedLink,
    onEditLinkClick: (Long, SavedLink) -> Unit,
    onRequestDeleteLink: (Long, Long) -> Unit,
    onSavedLinkClick: (Long, Long) -> Unit,
) {
    val accentColor = FriendlyCardDefaults.groupAccentColor(groupId)

    SwipeActionBox(
        editContentDescription = stringResource(R.string.edit_link),
        deleteContentDescription = stringResource(R.string.delete_link),
        onEdit = { onEditLinkClick(groupId, link) },
        onDelete = { onRequestDeleteLink(groupId, link.id) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSavedLinkClick(groupId, link.id) }
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                color = accentColor.copy(alpha = 0.14f),
                contentColor = accentColor,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(end = 10.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Link,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(18.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = link.name,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = link.url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Horizontally draggable list item that reveals edit and delete actions.
 *
 * @param editContentDescription Accessibility description for the edit action.
 * @param deleteContentDescription Accessibility description for the delete action.
 * @param onEdit Called when the item is dragged far enough to the right.
 * @param onDelete Called when the item is dragged far enough to the left.
 * @param modifier Optional modifier for the swipe container.
 * @param content Foreground item content that follows the user's drag.
 */
@Composable
private fun SwipeActionBox(
    editContentDescription: String,
    deleteContentDescription: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var itemWidth by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableFloatStateOf(0f) }
    val maxOffset = itemWidth * 0.5f
    val actionThreshold = itemWidth * 0.45f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .onSizeChanged { itemWidth = it.width.toFloat() },
    ) {
        SwipeActionBackground(
            offset = offset,
            editContentDescription = editContentDescription,
            deleteContentDescription = deleteContentDescription,
        )
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.roundToInt(), y = 0) }
                .pointerInput(itemWidth) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when {
                                offset >= actionThreshold -> onEdit()
                                offset <= -actionThreshold -> onDelete()
                            }
                            offset = 0f
                        },
                        onDragCancel = {
                            offset = 0f
                        },
                    ) { change, dragAmount ->
                        change.consume()
                        offset = (offset + dragAmount).coerceIn(-maxOffset, maxOffset)
                    }
                },
        ) {
            content()
        }
    }
}

/**
 * Action background shown behind a swiped list item.
 *
 * @param offset Current foreground content offset in pixels.
 * @param editContentDescription Accessibility description for the edit action.
 * @param deleteContentDescription Accessibility description for the delete action.
 */
@Composable
private fun BoxScope.SwipeActionBackground(
    offset: Float,
    editContentDescription: String,
    deleteContentDescription: String,
) {
    when {
        offset > 0f -> SwipeActionSurface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            icon = Icons.Outlined.Edit,
            contentDescription = editContentDescription,
            alignment = Alignment.CenterStart,
        )

        offset < 0f -> SwipeActionSurface(
            color = MaterialTheme.colorScheme.errorContainer,
            icon = Icons.Outlined.Delete,
            contentDescription = deleteContentDescription,
            alignment = Alignment.CenterEnd,
        )
    }
}

/**
 * Colored swipe action surface with an icon pinned to the active swipe side.
 *
 * @param color Background color for the action.
 * @param icon Icon that identifies the action.
 * @param contentDescription Accessibility description for the icon.
 * @param alignment Side where the action icon should be shown.
 */
@Composable
private fun BoxScope.SwipeActionSurface(
    color: Color,
    icon: ImageVector,
    contentDescription: String,
    alignment: Alignment,
) {
    Box(
        modifier = Modifier
            .matchParentSize()
            .clip(MaterialTheme.shapes.large)
            .background(color)
            .padding(horizontal = 24.dp),
        contentAlignment = alignment,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
