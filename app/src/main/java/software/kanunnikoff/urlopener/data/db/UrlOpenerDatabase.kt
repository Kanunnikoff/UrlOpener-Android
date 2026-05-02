package software.kanunnikoff.urlopener.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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
