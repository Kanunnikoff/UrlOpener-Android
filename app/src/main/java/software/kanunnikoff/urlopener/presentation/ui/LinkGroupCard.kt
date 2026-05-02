package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink

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
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onGroupClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.name,
                        fontWeight = FontWeight.Bold,
                    )
                    if (group.description.isNotBlank()) {
                        Text(
                            text = group.description,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = if (isExpanded) {
                        stringResource(R.string.collapse_group)
                    } else {
                        stringResource(R.string.expand_group)
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
                IconButton(onClick = { onAddLinkClick(group.id) }) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.add_link),
                    )
                }
                IconButton(onClick = { onEditGroupClick(group) }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.edit_group),
                    )
                }
                IconButton(onClick = { onRequestDeleteGroup(group.id) }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.delete_group),
                    )
                }
            }

            if (isExpanded) {
                HorizontalDivider()
                if (group.links.isEmpty()) {
                    Text(text = stringResource(R.string.empty_links_message))
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSavedLinkClick(groupId, link.id) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Link,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = link.name)
            Text(
                text = link.url,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        IconButton(onClick = { onEditLinkClick(groupId, link) }) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.edit_link),
            )
        }
        IconButton(onClick = { onRequestDeleteLink(groupId, link.id) }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.delete_link),
            )
        }
    }
}
