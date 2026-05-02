package software.kanunnikoff.urlopener.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R

/**
 * About tab with the app name and build version.
 *
 * @param appVersionName Human-readable app version.
 * @param appVersionCode Numeric build version.
 * @param modifier Optional modifier for the tab content.
 */
@Composable
internal fun AboutScreen(
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
        Spacer(modifier = Modifier.weight(1f))
    }
}
