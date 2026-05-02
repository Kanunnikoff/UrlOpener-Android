package software.kanunnikoff.urlopener.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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
    @Query("SELECT * FROM link_groups ORDER BY createdAt DESC")
    fun observeGroups(): Flow<List<GroupWithLinks>>

    @Insert
    suspend fun insertGroup(group: LinkGroupEntity)

    @Query(
        """
        UPDATE link_groups
        SET name = :name, description = :description, updatedAt = :updatedAt
        WHERE id = :groupId
        """,
    )
    suspend fun updateGroup(groupId: Long, name: String, description: String, updatedAt: Long)

    @Query("DELETE FROM link_groups WHERE id = :groupId")
    suspend fun deleteGroup(groupId: Long)

    @Insert
    suspend fun insertLink(link: SavedLinkEntity)

    @Query(
        """
        UPDATE saved_links
        SET name = :name, url = :url, updatedAt = :updatedAt
        WHERE id = :linkId AND groupId = :groupId
        """,
    )
    suspend fun updateLink(groupId: Long, linkId: Long, name: String, url: String, updatedAt: Long)

    @Query("DELETE FROM saved_links WHERE id = :linkId AND groupId = :groupId")
    suspend fun deleteLink(groupId: Long, linkId: Long)
}
