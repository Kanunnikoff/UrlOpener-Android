package software.kanunnikoff.urlopener.presentation

import software.kanunnikoff.urlopener.presentation.model.UrlOpenerTab

data class UrlOpenerState(
    val url: String = "",
    val selectedTab: UrlOpenerTab = UrlOpenerTab.Home,
    val shouldAskDeleteConfirmation: Boolean = true,
)
