package software.kanunnikoff.urlopener.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * App-level Material theme wrapper.
 *
 * The color scheme follows the system dark-mode setting by default and wraps content in a Surface
 * so every screen receives the theme background.
 */
@Composable
fun UrlOpenerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFFFFB77C),
            onPrimary = Color(0xFF452100),
            primaryContainer = Color(0xFF653000),
            onPrimaryContainer = Color(0xFFFFDCC3),
            secondary = Color(0xFF91D6B7),
            onSecondary = Color(0xFF003827),
            secondaryContainer = Color(0xFF16513D),
            onSecondaryContainer = Color(0xFFADEFCF),
            tertiary = Color(0xFFFFC9DD),
            onTertiary = Color(0xFF4F1129),
            tertiaryContainer = Color(0xFF6A2940),
            onTertiaryContainer = Color(0xFFFFD8E7),
            background = Color(0xFF15130F),
            onBackground = Color(0xFFEDE1D5),
            surface = Color(0xFF1F1B16),
            onSurface = Color(0xFFF4E8DC),
            surfaceVariant = Color(0xFF51443A),
            onSurfaceVariant = Color(0xFFD7C4B6),
            outline = Color(0xFFA28F82),
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF9D4F00),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFFDCC3),
            onPrimaryContainer = Color(0xFF321300),
            secondary = Color(0xFF2F6B53),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFB2F1D0),
            onSecondaryContainer = Color(0xFF002116),
            tertiary = Color(0xFF8D425F),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFFD8E7),
            onTertiaryContainer = Color(0xFF3A071D),
            background = Color(0xFFFFF8F1),
            onBackground = Color(0xFF211A15),
            surface = Color(0xFFFFF8F1),
            onSurface = Color(0xFF211A15),
            surfaceVariant = Color(0xFFF3DED0),
            onSurfaceVariant = Color(0xFF51443A),
            outline = Color(0xFF837468),
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(8.dp),
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp),
            extraLarge = RoundedCornerShape(32.dp),
        ),
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorScheme.background,
                content = content,
            )
        },
    )
}
