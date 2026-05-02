package software.kanunnikoff.urlopener

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application root used by Hilt to create the app-level dependency graph.
 */
@HiltAndroidApp
class UrlOpenerApplication : Application()
