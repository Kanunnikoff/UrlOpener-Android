package software.kanunnikoff.urlopener.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Room access object for groups and saved links.
 *
 * Reads are wrapped in a transaction so every emission contains matching groups and links from the
 * same database snapshot.
 */
@Dao
interface LinkGroupsDao {

    @Transaction
    @Query("SELECT * FROM link_groups ORDER BY name")
    fun observeGroups(): Flow<List<GroupWithLinks>>

    @Insert
    suspend fun insertGroup(group: LinkGroupEntity)

    @Update
    suspend fun updateGroup(group: LinkGroupEntity)

    @Query("DELETE FROM link_groups WHERE id = :groupId")
    suspend fun deleteGroup(groupId: Long)

    @Insert
    suspend fun insertLink(link: SavedLinkEntity)

    @Update
    suspend fun updateLink(link: SavedLinkEntity)

    @Query("DELETE FROM saved_links WHERE id = :linkId AND groupId = :groupId")
    suspend fun deleteLink(groupId: Long, linkId: Long)
}
