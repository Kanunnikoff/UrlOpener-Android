package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import software.kanunnikoff.urlopener.R
import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab

/**
 * Bottom navigation that switches between the app's top-level tabs.
 *
 * @param selectedTab Currently selected tab.
 * @param onTabSelected Called when the user selects a tab.
 */
@Composable
internal fun UrlOpenerNavigationBar(
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
