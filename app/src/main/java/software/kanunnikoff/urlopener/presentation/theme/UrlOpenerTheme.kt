package software.kanunnikoff.urlopener.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            primary = Color(color = 0xFFFFB77C),
            onPrimary = Color(color = 0xFF452100),
            primaryContainer = Color(color = 0xFF653000),
            onPrimaryContainer = Color(color = 0xFFFFDCC3),
            secondary = Color(color = 0xFF91D6B7),
            onSecondary = Color(color = 0xFF003827),
            secondaryContainer = Color(color = 0xFF16513D),
            onSecondaryContainer = Color(color = 0xFFADEFCF),
            tertiary = Color(color = 0xFFFFC9DD),
            onTertiary = Color(color = 0xFF4F1129),
            tertiaryContainer = Color(color = 0xFF6A2940),
            onTertiaryContainer = Color(color = 0xFFFFD8E7),
            background = Color(color = 0xFF15130F),
            onBackground = Color(color = 0xFFEDE1D5),
            surface = Color(color = 0xFF1F1B16),
            onSurface = Color(color = 0xFFF4E8DC),
            surfaceVariant = Color(color = 0xFF51443A),
            onSurfaceVariant = Color(color = 0xFFD7C4B6),
            outline = Color(color = 0xFFA28F82),
        )
    } else {
        lightColorScheme(
            primary = Color(color = 0xFF9D4F00),
            onPrimary = Color.White,
            primaryContainer = Color(color = 0xFFFFDCC3),
            onPrimaryContainer = Color(color = 0xFF321300),
            secondary = Color(color = 0xFF2F6B53),
            onSecondary = Color.White,
            secondaryContainer = Color(color = 0xFFB2F1D0),
            onSecondaryContainer = Color(color = 0xFF002116),
            tertiary = Color(color = 0xFF8D425F),
            onTertiary = Color.White,
            tertiaryContainer = Color(color = 0xFFFFD8E7),
            onTertiaryContainer = Color(color = 0xFF3A071D),
            background = Color(color = 0xFFFFF8F1),
            onBackground = Color(color = 0xFF211A15),
            surface = Color(color = 0xFFFFF8F1),
            onSurface = Color(color = 0xFF211A15),
            surfaceVariant = Color(color = 0xFFF3DED0),
            onSurfaceVariant = Color(color = 0xFF51443A),
            outline = Color(color = 0xFF837468),
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(size = 8.dp),
            small = RoundedCornerShape(size = 12.dp),
            medium = RoundedCornerShape(size = 16.dp),
            large = RoundedCornerShape(size = 24.dp),
            extraLarge = RoundedCornerShape(size = 32.dp),
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
