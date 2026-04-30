package software.kanunnikoff.urlopener

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import software.kanunnikoff.urlopener.presentation.UrlOpenerState
import software.kanunnikoff.urlopener.presentation.theme.UrlOpenerTheme
import software.kanunnikoff.urlopener.presentation.ui.UrlOpenerScreen

class UrlOpenerScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun clearButtonRemovesEnteredUrl() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val url = "https://example.com"

        composeRule.setContent {
            UrlOpenerTheme {
                var state by remember { mutableStateOf(UrlOpenerState()) }

                UrlOpenerScreen(
                    state = state,
                    snackbarHostState = SnackbarHostState(),
                    appVersionName = "1.0.0",
                    appVersionCode = 1,
                    onTabSelected = { state = state.copy(selectedTab = it) },
                    onUrlChanged = { state = state.copy(url = it) },
                    onClearClick = { state = state.copy(url = "") },
                    onOpenClick = {},
                    onDeleteConfirmationChanged = {
                        state = state.copy(shouldAskDeleteConfirmation = it)
                    },
                    onOpenConfirmationChanged = {
                        state = state.copy(shouldAskOpenConfirmation = it)
                    },
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

        composeRule.onNodeWithText(context.getString(R.string.url_input_description))
            .performTextInput(url)

        composeRule.onNodeWithText(url)
            .assertTextContains(url)

        composeRule.onNodeWithText(context.getString(R.string.clear_button))
            .performClick()

        // После очистки остаётся только подпись поля; введённой ссылки в дереве Compose быть не должно.
        composeRule.onNodeWithText(url)
            .assertDoesNotExist()
    }
}
