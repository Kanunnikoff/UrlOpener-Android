package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.presentation.GroupEditorState
import software.kanunnikoff.urlopener.presentation.LinkEditorState

/**
 * Dialog for creating a new group or editing an existing one.
 *
 * @param editor Current group editor state with initial field values.
 * @param onSaveGroup Called with the entered name and description when the dialog is confirmed.
 * @param onDismiss Called when the dialog should be closed without saving.
 */
@Composable
internal fun GroupEditorDialog(
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

/**
 * Dialog for creating a new saved link or editing an existing one.
 *
 * @param editor Current link editor state with initial field values.
 * @param onSaveLink Called with the group id, entered name, and URL when the dialog is confirmed.
 * @param onDismiss Called when the dialog should be closed without saving.
 */
@Composable
internal fun LinkEditorDialog(
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

/**
 * Dialog that asks which group should receive the currently entered URL.
 *
 * @param groups Available groups that can receive the saved link.
 * @param onGroupPicked Called with the selected group id.
 * @param onDismiss Called when the dialog should be closed without choosing a group.
 */
@Composable
internal fun GroupPickerDialog(
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

/**
 * Shared confirmation dialog for destructive actions and saved-link opening.
 *
 * @param title Dialog title text.
 * @param text Dialog body text.
 * @param confirmText Text for the confirmation button.
 * @param onConfirm Called when the user confirms the action.
 * @param onDismiss Called when the dialog should be closed without confirming.
 */
@Composable
internal fun ConfirmationDialog(
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
