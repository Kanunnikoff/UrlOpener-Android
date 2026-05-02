package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
    ) {
        NavigationBarItem(
            selected = selectedTab == UrlOpenerTab.Home,
            onClick = { onTabSelected(UrlOpenerTab.Home) },
            colors = friendlyNavigationItemColors(),
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = stringResource(R.string.home_tab),
                )
            },
            alwaysShowLabel = false,
        )
        NavigationBarItem(
            selected = selectedTab == UrlOpenerTab.Settings,
            onClick = { onTabSelected(UrlOpenerTab.Settings) },
            colors = friendlyNavigationItemColors(),
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.settings_tab),
                )
            },
            alwaysShowLabel = false,
        )
        NavigationBarItem(
            selected = selectedTab == UrlOpenerTab.About,
            onClick = { onTabSelected(UrlOpenerTab.About) },
            colors = friendlyNavigationItemColors(),
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.about_tab),
                )
            },
            alwaysShowLabel = false,
        )
    }
}

/**
 * Shared bottom-navigation colors for the Friendly Cards design.
 *
 * @return Navigation item colors that keep selected tabs warm and visible in both themes.
 */
@Composable
private fun friendlyNavigationItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
    selectedTextColor = MaterialTheme.colorScheme.onSurface,
    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
)
