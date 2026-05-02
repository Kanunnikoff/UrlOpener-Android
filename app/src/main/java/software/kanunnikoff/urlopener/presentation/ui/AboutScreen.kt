package software.kanunnikoff.urlopener.presentation.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import software.kanunnikoff.urlopener.R

/**
 * About tab with app identity, store actions, and support contacts.
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
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        ScreenTitle(text = stringResource(R.string.about_tab))

        AppIdentityHeader(
            appVersionName = appVersionName,
            appVersionCode = appVersionCode,
        )

        AboutSection(
            title = stringResource(R.string.about_google_play_title),
            footer = stringResource(R.string.about_google_play_footer),
        ) {
            AboutActionRow(
                text = stringResource(R.string.about_rate),
                onClick = { context.openUrlWithFallback(PLAY_MARKET_REVIEW_URI, PLAY_STORE_APP_URL) },
            )

            AboutDivider()

            AboutActionRow(
                text = stringResource(R.string.about_share),
                onClick = { context.shareApp() },
            )

            AboutDivider()

            AboutActionRow(
                text = stringResource(R.string.about_other_apps),
                onClick = { context.openUrl(PLAY_STORE_DEVELOPER_URL) },
            )
        }

        AboutSection(
            title = stringResource(R.string.about_support_title),
            footer = stringResource(R.string.about_support_footer),
        ) {
            AboutActionRow(
                text = stringResource(R.string.about_write_letter),
                onClick = { context.openUrl(DEVELOPER_EMAIL_URI) },
            )
        }

        AboutSection(
            title = stringResource(R.string.about_legal_title),
            footer = stringResource(R.string.about_legal_footer),
        ) {
            AboutActionRow(
                text = stringResource(R.string.about_terms_of_service),
                onClick = { context.openUrl(TERMS_OF_SERVICE_URL) },
            )

            AboutDivider()

            AboutActionRow(
                text = stringResource(R.string.about_privacy_policy),
                onClick = { context.openUrl(PRIVACY_POLICY_URL) },
            )
        }
    }
}

@Composable
private fun AppIdentityHeader(
    appVersionName: String,
    appVersionCode: Int,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = stringResource(
                        R.string.app_version_with_build,
                        appVersionName,
                        appVersionCode,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )

                Text(
                    text = stringResource(R.string.about_copyright),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun AboutSection(
    title: String,
    footer: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(
                content = content,
            )
        }

        Text(
            text = footer,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun AboutDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline.copy(alpha = ABOUT_DIVIDER_ALPHA),
    )
}

@Composable
private fun AboutActionRow(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
        )

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

private fun Context.openUrlWithFallback(primaryUrl: String, fallbackUrl: String) {
    try {
        openUrl(primaryUrl)
    } catch (exception: ActivityNotFoundException) {
        openUrl(fallbackUrl)
    }
}

private fun Context.shareApp() {
    val shareIntent = Intent(Intent.ACTION_SEND)
        .setType(SHARE_MIME_TYPE)
        .putExtra(Intent.EXTRA_TEXT, PLAY_STORE_APP_URL)

    startActivity(
        Intent.createChooser(
            shareIntent,
            getString(R.string.about_share),
        ),
    )
}

private const val APPLICATION_ID = "software.kanunnikoff.urlopener"
private const val ABOUT_DIVIDER_ALPHA = 0.28f
private const val SHARE_MIME_TYPE = "text/plain"
private const val PLAY_MARKET_REVIEW_URI = "market://details?id=$APPLICATION_ID"
private const val PLAY_STORE_APP_URL = "https://play.google.com/store/apps/details?id=$APPLICATION_ID"
private const val PLAY_STORE_DEVELOPER_URL = "https://play.google.com/store/apps/developer?id=Dmitry+Kanunnikoff"
private const val DEVELOPER_EMAIL_URI = "mailto:dmitry.kanunnikoff@gmail.com?subject=UrlOpener%20(Android)"
private const val TERMS_OF_SERVICE_URL = "https://example.com/urlopener-terms"
private const val PRIVACY_POLICY_URL = "https://example.com/urlopener-privacy"
