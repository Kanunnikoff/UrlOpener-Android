package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Shared color helpers for the Friendly Cards design direction.
 */
internal object FriendlyCardDefaults {

    /**
     * Returns a stable accent color for a group based on its identifier.
     *
     * @param groupId Stable group identifier from the database.
     * @return Accent color used by group markers and icons.
     */
    @Composable
    fun groupAccentColor(groupId: Long): Color {
        val colors = listOf(
            Color(color = 0xFFEF8E52),
            Color(color = 0xFF55A879),
            Color(color = 0xFF7B8DE8),
            Color(color = 0xFFD66C9F),
            Color(color = 0xFFB08A37),
        )

        return colors[(groupId % colors.size).toInt()]
    }
}
