package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R
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
    modifier: Modifier = Modifier,
) {
    Scaffold(
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
                modifier = Modifier.padding(paddingValues),
            )

            UrlOpenerTab.Settings -> SettingsScreen(
                shouldAskDeleteConfirmation = state.shouldAskDeleteConfirmation,
                onDeleteConfirmationChanged = onDeleteConfirmationChanged,
                modifier = Modifier.padding(paddingValues),
            )

            UrlOpenerTab.About -> AboutScreen(
                appVersionName = appVersionName,
                appVersionCode = appVersionCode,
                modifier = Modifier.padding(paddingValues),
            )
        }
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = state.url,
            onValueChange = onUrlChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(stringResource(R.string.url_input_description)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done,
            ),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(onClick = onClearClick) {
                Text(stringResource(R.string.clear_button))
            }

            Button(onClick = onOpenClick) {
                Text(stringResource(R.string.open_button))
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    shouldAskDeleteConfirmation: Boolean,
    onDeleteConfirmationChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.ask_delete_confirmation),
                modifier = Modifier.weight(1f),
            )

            Switch(
                checked = shouldAskDeleteConfirmation,
                onCheckedChange = onDeleteConfirmationChanged,
            )
        }
    }
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
    }
}

@Preview(showBackground = true)
@Composable
private fun UrlOpenerScreenPreview() {
    UrlOpenerTheme {
        UrlOpenerScreen(
            state = UrlOpenerState(url = "https://example.com"),
            snackbarHostState = SnackbarHostState(),
            appVersionName = "1.0.0",
            appVersionCode = 1,
            onTabSelected = {},
            onUrlChanged = {},
            onClearClick = {},
            onOpenClick = {},
            onDeleteConfirmationChanged = {},
        )
    }
}
