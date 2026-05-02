package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R

/**
 * Settings tab for toggling confirmation prompts.
 *
 * @param shouldAskDeleteConfirmation Whether delete actions require confirmation.
 * @param shouldAskOpenConfirmation Whether opening saved links requires confirmation.
 * @param onDeleteConfirmationChanged Called when the delete-confirmation switch changes.
 * @param onOpenConfirmationChanged Called when the open-confirmation switch changes.
 * @param modifier Optional modifier for the tab content.
 */
@Composable
internal fun SettingsScreen(
    shouldAskDeleteConfirmation: Boolean,
    shouldAskOpenConfirmation: Boolean,
    onDeleteConfirmationChanged: (Boolean) -> Unit,
    onOpenConfirmationChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        ScreenTitle(
            text = stringResource(R.string.settings_tab),
        )
        SettingsSwitchRow(
            text = stringResource(R.string.ask_delete_confirmation),
            checked = shouldAskDeleteConfirmation,
            onCheckedChange = onDeleteConfirmationChanged,
            icon = Icons.Outlined.Delete,
        )
        SettingsSwitchRow(
            text = stringResource(R.string.ask_open_confirmation),
            checked = shouldAskOpenConfirmation,
            onCheckedChange = onOpenConfirmationChanged,
            icon = Icons.AutoMirrored.Outlined.OpenInNew,
        )
    }
}

/**
 * One settings row with a text label and a switch aligned to the end.
 *
 * @param text Label shown before the switch.
 * @param checked Current switch value.
 * @param onCheckedChange Called when the switch value changes.
 * @param icon Icon that visually identifies the setting.
 */
@Composable
private fun SettingsSwitchRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                )
            }
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    }
}
