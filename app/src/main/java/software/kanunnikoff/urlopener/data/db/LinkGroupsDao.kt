package software.kanunnikoff.urlopener.data.db

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
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
    suspend fun insertGroup(group: LinkGroupEntity): Long

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

    @Query("DELETE FROM saved_links")
    suspend fun deleteAllLinks()

    @Query("DELETE FROM link_groups")
    suspend fun deleteAllGroups()

    @Transaction
    suspend fun replaceGroups(groups: List<GroupWithLinks>) {
        deleteAllLinks()
        deleteAllGroups()

        groups.forEach { groupWithLinks ->
            val groupId = insertGroup(groupWithLinks.group.copy(id = 0L))

            groupWithLinks.links.forEach { link ->
                insertLink(link.copy(id = 0L, groupId = groupId))
            }
        }
    }

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
