package software.kanunnikoff.urlopener.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import software.kanunnikoff.urlopener.data.AndroidLinkGroupsRepository
import software.kanunnikoff.urlopener.data.AndroidSettingsRepository
import software.kanunnikoff.urlopener.data.AndroidUrlOpenerRepository
import software.kanunnikoff.urlopener.data.GoogleDriveSyncRepository
import software.kanunnikoff.urlopener.data.db.LinkGroupsDao
import software.kanunnikoff.urlopener.data.db.UrlOpenerDatabase
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository
import software.kanunnikoff.urlopener.domain.repository.SettingsRepository
import software.kanunnikoff.urlopener.domain.repository.SyncRepository
import software.kanunnikoff.urlopener.domain.repository.UrlOpenerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UrlOpenerBindingsModule {

    @Binds
    @Singleton
    abstract fun bindLinkGroupsRepository(repository: AndroidLinkGroupsRepository): LinkGroupsRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(repository: AndroidSettingsRepository): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindSyncRepository(repository: GoogleDriveSyncRepository): SyncRepository

    @Binds
    @Singleton
    abstract fun bindUrlOpenerRepository(repository: AndroidUrlOpenerRepository): UrlOpenerRepository
}

@Module
@InstallIn(SingletonComponent::class)
object UrlOpenerDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): UrlOpenerDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = UrlOpenerDatabase::class.java,
            name = DATABASE_NAME,
        ).build()
    }

    @Provides
    fun provideLinkGroupsDao(database: UrlOpenerDatabase): LinkGroupsDao {
        return database.linkGroupsDao()
    }

    private const val DATABASE_NAME = "url_opener.db"
}
