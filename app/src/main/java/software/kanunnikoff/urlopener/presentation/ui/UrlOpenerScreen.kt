package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LinkOff
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.presentation.DeleteTarget
import software.kanunnikoff.urlopener.presentation.GroupEditorState
import software.kanunnikoff.urlopener.presentation.LinkEditorState
import software.kanunnikoff.urlopener.presentation.UrlOpenerState
import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab
import software.kanunnikoff.urlopener.presentation.theme.UrlOpenerTheme

@Composable
fun UrlOpenerScreen(
    state: UrlOpenerState,
    snackbarHostState: SnackbarHostState,
    appVersionName: String,
    appVersionCode: Int,
    onTabSelected: (UrlOpenerTab) -> Unit,
    onUrlChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onOpenClick: () -> Unit,
    onDeleteConfirmationChanged: (Boolean) -> Unit,
    onOpenConfirmationChanged: (Boolean) -> Unit,
    onAddGroupClick: () -> Unit,
    onEditGroupClick: (LinkGroup) -> Unit,
    onRequestDeleteGroup: (Long) -> Unit,
    onSaveGroup: (String, String) -> Unit,
    onDismissGroupEditor: () -> Unit,
    onAddLinkClick: (Long) -> Unit,
    onSaveEnteredLinkClick: () -> Unit,
    onGroupPickedForEnteredLink: (Long) -> Unit,
    onDismissGroupPicker: () -> Unit,
    onEditLinkClick: (Long, SavedLink) -> Unit,
    onRequestDeleteLink: (Long, Long) -> Unit,
    onSaveLink: (Long, String, String) -> Unit,
    onDismissLinkEditor: () -> Unit,
    onSavedLinkClick: (Long, Long) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDeleteConfirmation: () -> Unit,
    onConfirmOpenSavedLink: () -> Unit,
    onDismissOpenConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            UrlOpenerNavigationBar(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        when (state.selectedTab) {
            UrlOpenerTab.Home -> HomeScreen(
                state = state,
                onUrlChanged = onUrlChanged,
                onClearClick = onClearClick,
                onOpenClick = onOpenClick,
                onSaveEnteredLinkClick = onSaveEnteredLinkClick,
                onAddGroupClick = onAddGroupClick,
                onEditGroupClick = onEditGroupClick,
                onRequestDeleteGroup = onRequestDeleteGroup,
                onAddLinkClick = onAddLinkClick,
                onEditLinkClick = onEditLinkClick,
                onRequestDeleteLink = onRequestDeleteLink,
                onSavedLinkClick = onSavedLinkClick,
                contentPadding = paddingValues,
            )

            UrlOpenerTab.Settings -> SettingsScreen(
                shouldAskDeleteConfirmation = state.shouldAskDeleteConfirmation,
                shouldAskOpenConfirmation = state.shouldAskOpenConfirmation,
                onDeleteConfirmationChanged = onDeleteConfirmationChanged,
                onOpenConfirmationChanged = onOpenConfirmationChanged,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
            )

            UrlOpenerTab.About -> AboutScreen(
                appVersionName = appVersionName,
                appVersionCode = appVersionCode,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
            )
        }
    }

    state.groupEditor?.let { editor ->
        GroupEditorDialog(
            editor = editor,
            onSaveGroup = onSaveGroup,
            onDismiss = onDismissGroupEditor,
        )
    }

    state.linkEditor?.let { editor ->
        LinkEditorDialog(
            editor = editor,
            onSaveLink = onSaveLink,
            onDismiss = onDismissLinkEditor,
        )
    }

    if (state.shouldShowGroupPicker) {
        GroupPickerDialog(
            groups = state.groups,
            onGroupPicked = onGroupPickedForEnteredLink,
            onDismiss = onDismissGroupPicker,
        )
    }

    state.deleteTarget?.let {
        ConfirmationDialog(
            title = stringResource(R.string.delete_confirmation_title),
            text = when (it) {
                is DeleteTarget.Group -> stringResource(R.string.delete_group_confirmation)
                is DeleteTarget.Link -> stringResource(R.string.delete_link_confirmation)
            },
            confirmText = stringResource(R.string.delete_button),
            onConfirm = onConfirmDelete,
            onDismiss = onDismissDeleteConfirmation,
        )
    }

    state.openTarget?.let { target ->
        ConfirmationDialog(
            title = stringResource(R.string.open_confirmation_title),
            text = stringResource(R.string.open_link_confirmation, target.link.name),
            confirmText = stringResource(R.string.open_button),
            onConfirm = onConfirmOpenSavedLink,
            onDismiss = onDismissOpenConfirmation,
        )
    }
}

@Composable
private fun UrlOpenerNavigationBar(
    selectedTab: UrlOpenerTab,
    onTabSelected: (UrlOpenerTab) -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == UrlOpenerTab.Home,
            onClick = { onTabSelected(UrlOpenerTab.Home) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = stringResource(R.string.home_tab),
                )
            },
            label = { Text(stringResource(R.string.home_tab)) },
        )
        NavigationBarItem(
            selected = selectedTab == UrlOpenerTab.Settings,
            onClick = { onTabSelected(UrlOpenerTab.Settings) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.settings_tab),
                )
            },
            label = { Text(stringResource(R.string.settings_tab)) },
        )
        NavigationBarItem(
            selected = selectedTab == UrlOpenerTab.About,
            onClick = { onTabSelected(UrlOpenerTab.About) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.about_tab),
                )
            },
            label = { Text(stringResource(R.string.about_tab)) },
        )
    }
}

@Composable
private fun HomeScreen(
    state: UrlOpenerState,
    onUrlChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onOpenClick: () -> Unit,
    onSaveEnteredLinkClick: () -> Unit,
    onAddGroupClick: () -> Unit,
    onEditGroupClick: (LinkGroup) -> Unit,
    onRequestDeleteGroup: (Long) -> Unit,
    onAddLinkClick: (Long) -> Unit,
    onEditLinkClick: (Long, SavedLink) -> Unit,
    onRequestDeleteLink: (Long, Long) -> Unit,
    onSavedLinkClick: (Long, Long) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val listPadding = contentPadding.withAdditionalPadding(20.dp)
    var expandedGroupIds by rememberSaveable { mutableStateOf(emptySet<Long>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .consumeWindowInsets(contentPadding)
            .padding(listPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        UrlInputBlock(
            url = state.url,
            onUrlChanged = onUrlChanged,
            onClearClick = onClearClick,
            onOpenClick = onOpenClick,
            onSaveEnteredLinkClick = onSaveEnteredLinkClick,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.saved_links_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            TextButton(onClick = onAddGroupClick) {
                Text(stringResource(R.string.add_group))
            }
        }

        if (state.groups.isEmpty()) {
            EmptyGroupsContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = state.groups,
                    key = { it.id },
                ) { group ->
                    val isExpanded = group.id in expandedGroupIds
                    LinkGroupCard(
                        group = group,
                        isExpanded = isExpanded,
                        onGroupClick = {
                            expandedGroupIds = if (isExpanded) {
                                expandedGroupIds - group.id
                            } else {
                                expandedGroupIds + group.id
                            }
                        },
                        onEditGroupClick = onEditGroupClick,
                        onRequestDeleteGroup = onRequestDeleteGroup,
                        onAddLinkClick = onAddLinkClick,
                        onEditLinkClick = onEditLinkClick,
                        onRequestDeleteLink = onRequestDeleteLink,
                        onSavedLinkClick = onSavedLinkClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyGroupsContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.LinkOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(72.dp),
            )
            Text(
                text = stringResource(R.string.empty_groups_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.empty_groups_message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PaddingValues.withAdditionalPadding(all: Dp): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + all,
        top = calculateTopPadding() + all,
        end = calculateEndPadding(layoutDirection) + all,
        bottom = calculateBottomPadding() + all,
    )
}

@Composable
private fun UrlInputBlock(
    url: String,
    onUrlChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onOpenClick: () -> Unit,
    onSaveEnteredLinkClick: () -> Unit,
) {
    val hasUrl = url.isNotBlank()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChanged,
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 3,
            label = { Text(stringResource(R.string.url_input_description)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Default,
            ),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                enabled = hasUrl,
                onClick = onClearClick,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.clear_button))
            }

            Button(
                enabled = hasUrl,
                onClick = onOpenClick,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.open_button))
            }

            Button(
                enabled = hasUrl,
                onClick = onSaveEnteredLinkClick,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.save_button))
            }
        }
    }
}

@Composable
private fun LinkGroupCard(
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

@Composable
private fun SettingsScreen(
    shouldAskDeleteConfirmation: Boolean,
    shouldAskOpenConfirmation: Boolean,
    onDeleteConfirmationChanged: (Boolean) -> Unit,
    onOpenConfirmationChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SettingsSwitchRow(
            text = stringResource(R.string.ask_delete_confirmation),
            checked = shouldAskDeleteConfirmation,
            onCheckedChange = onDeleteConfirmationChanged,
        )
        SettingsSwitchRow(
            text = stringResource(R.string.ask_open_confirmation),
            checked = shouldAskOpenConfirmation,
            onCheckedChange = onOpenConfirmationChanged,
        )
    }
}

@Composable
private fun SettingsSwitchRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun GroupEditorDialog(
    editor: GroupEditorState,
    onSaveGroup: (String, String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember(editor) { mutableStateOf(editor.name) }
    var description by remember(editor) { mutableStateOf(editor.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (editor.groupId == null) {
                    stringResource(R.string.add_group)
                } else {
                    stringResource(R.string.edit_group)
                },
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.group_name)) },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.group_description)) },
                    minLines = 3,
                    maxLines = 3,
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onSaveGroup(name, description) },
            ) {
                Text(stringResource(R.string.save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        },
    )
}

@Composable
private fun LinkEditorDialog(
    editor: LinkEditorState,
    onSaveLink: (Long, String, String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember(editor) { mutableStateOf(editor.name) }
    var url by remember(editor) { mutableStateOf(editor.url) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (editor.linkId == null) {
                    stringResource(R.string.add_link)
                } else {
                    stringResource(R.string.edit_link)
                },
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.link_name)) },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text(stringResource(R.string.url_input_description)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done,
                    ),
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank() && url.isNotBlank(),
                onClick = { onSaveLink(editor.groupId, name, url) },
            ) {
                Text(stringResource(R.string.save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        },
    )
}

@Composable
private fun GroupPickerDialog(
    groups: List<LinkGroup>,
    onGroupPicked: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.choose_group)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                groups.forEach { group ->
                    TextButton(onClick = { onGroupPicked(group.id) }) {
                        Text(group.name)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        },
    )
}

@Composable
private fun ConfirmationDialog(
    title: String,
    text: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        },
    )
}

@Composable
private fun AboutScreen(
    appVersionName: String,
    appVersionCode: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontWeight = FontWeight.Bold,
        )

        HorizontalDivider()

        Text(
            text = stringResource(
                R.string.app_version_with_build,
                appVersionName,
                appVersionCode,
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
private fun UrlOpenerScreenPreview() {
    UrlOpenerTheme {
        UrlOpenerScreen(
            state = UrlOpenerState(
                url = "https://example.com",
                groups = listOf(
                    LinkGroup(
                        id = 1L,
                        name = "Работа",
                        description = "Служебные ссылки",
                        links = listOf(
                            SavedLink(
                                id = 1L,
                                name = "Почта",
                                url = "https://mail.example",
                            ),
                        ),
                    ),
                ),
            ),
            snackbarHostState = SnackbarHostState(),
            appVersionName = "1.0.0",
            appVersionCode = 1,
            onTabSelected = {},
            onUrlChanged = {},
            onClearClick = {},
            onOpenClick = {},
            onDeleteConfirmationChanged = {},
            onOpenConfirmationChanged = {},
            onAddGroupClick = {},
            onEditGroupClick = {},
            onRequestDeleteGroup = {},
            onSaveGroup = { _, _ -> },
            onDismissGroupEditor = {},
            onAddLinkClick = {},
            onSaveEnteredLinkClick = {},
            onGroupPickedForEnteredLink = {},
            onDismissGroupPicker = {},
            onEditLinkClick = { _, _ -> },
            onRequestDeleteLink = { _, _ -> },
            onSaveLink = { _, _, _ -> },
            onDismissLinkEditor = {},
            onSavedLinkClick = { _, _ -> },
            onConfirmDelete = {},
            onDismissDeleteConfirmation = {},
            onConfirmOpenSavedLink = {},
            onDismissOpenConfirmation = {},
        )
    }
}
