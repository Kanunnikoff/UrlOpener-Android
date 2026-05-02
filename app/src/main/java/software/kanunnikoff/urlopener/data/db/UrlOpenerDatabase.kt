package software.kanunnikoff.urlopener.data.db

import androidx.room3.Database
import androidx.room3.RoomDatabase

/**
 * Main Room database for the app.
 */
@Database(
    entities = [
        LinkGroupEntity::class,
        SavedLinkEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class UrlOpenerDatabase : RoomDatabase() {
    abstract fun linkGroupsDao(): LinkGroupsDao
}
