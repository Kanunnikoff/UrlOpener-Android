package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LinkOff
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.presentation.UrlOpenerState

/**
 * Home tab with URL input, group management entry points, and the saved-link list.
 *
 * @param state Current screen state containing the URL input and saved groups.
 * @param onUrlChanged Called when the URL input text changes.
 * @param onClearClick Called when the user clears the URL input.
 * @param onOpenClick Called when the user opens the entered URL.
 * @param onSaveEnteredLinkClick Called when the user saves the current URL input.
 * @param onAddGroupClick Called when the user starts creating a group.
 * @param onEditGroupClick Called when the user starts editing a group.
 * @param onRequestDeleteGroup Called when the user requests group deletion.
 * @param onAddLinkClick Called when the user starts adding a link to a group.
 * @param onEditLinkClick Called when the user starts editing a saved link.
 * @param onRequestDeleteLink Called when the user requests saved-link deletion.
 * @param onSavedLinkClick Called when the user selects a saved link for opening.
 * @param contentPadding Padding provided by the root scaffold.
 * @param modifier Optional modifier for the tab content.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeScreen(
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
    val listPadding = contentPadding.withAdditionalPadding(16.dp)
    var expandedGroupIds by rememberSaveable { mutableStateOf(emptySet<Long>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .consumeWindowInsets(contentPadding)
            .padding(listPadding),
    ) {
        ScreenTitle(text = stringResource(R.string.app_name))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(top = 12.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "url_input") {
                UrlInputBlock(
                    url = state.url,
                    onUrlChanged = onUrlChanged,
                    onClearClick = onClearClick,
                    onOpenClick = onOpenClick,
                    onSaveEnteredLinkClick = onSaveEnteredLinkClick,
                )
            }

            stickyHeader(key = "saved_links_header") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SavedLinksHeader(onAddGroupClick = onAddGroupClick)
                }
            }

            if (state.groups.isEmpty()) {
                item(key = "empty_groups") {
                    EmptyGroupsContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(fraction = 0.82f),
                    )
                }
            } else {
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

/**
 * Empty state shown when the user has not created any groups yet.
 *
 * @param modifier Optional modifier for positioning the empty state.
 */
@Composable
private fun EmptyGroupsContent(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.LinkOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
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
}

/**
 * Creates content padding that preserves safe-area insets and adds local spacing.
 *
 * @param all Additional padding added to every side.
 * @return Padding values that combine the existing insets with [all].
 */
@Composable
private fun PaddingValues.withAdditionalPadding(all: Dp): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current

    // Preserve the Scaffold-provided safe-area padding while adding local spacing around content.
    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + all,
        top = calculateTopPadding() + all,
        end = calculateEndPadding(layoutDirection) + all,
        bottom = calculateBottomPadding() + all,
    )
}

/**
 * URL input form with clear, open, and save actions.
 *
 * @param url Current URL input value.
 * @param onUrlChanged Called when the URL input text changes.
 * @param onClearClick Called when the user clears the URL input.
 * @param onOpenClick Called when the user opens the entered URL.
 * @param onSaveEnteredLinkClick Called when the user saves the current URL input.
 */
@Composable
private fun UrlInputBlock(
    url: String,
    onUrlChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onOpenClick: () -> Unit,
    onSaveEnteredLinkClick: () -> Unit,
) {
    val hasUrl = url.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = url,
                onValueChange = onUrlChanged,
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 3,
                label = { Text(stringResource(R.string.url_input_description)) },
                trailingIcon = {
                    if (hasUrl) {
                        IconButton(onClick = onClearClick) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.clear_button),
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Default,
                ),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilledTonalButton(
                    enabled = hasUrl,
                    onClick = onSaveEnteredLinkClick,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(stringResource(R.string.save_button))
                }

                Button(
                    enabled = hasUrl,
                    onClick = onOpenClick,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Link,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(stringResource(R.string.open_button))
                }
            }
        }
    }
}

/**
 * Section header for the saved-link list.
 *
 * @param onAddGroupClick Called when the user starts creating a new group.
 */
@Composable
private fun SavedLinksHeader(
    onAddGroupClick: () -> Unit,
) {
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

        TextButton(
            onClick = onAddGroupClick,
            colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(stringResource(R.string.add_group))
        }
    }
}
