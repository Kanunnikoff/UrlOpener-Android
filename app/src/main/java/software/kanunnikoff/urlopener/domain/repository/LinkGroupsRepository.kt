package software.kanunnikoff.urlopener.domain.repository

import kotlinx.coroutines.flow.Flow
import software.kanunnikoff.urlopener.domain.model.LinkGroup

/**
 * Provides the saved-link catalogue as a reactive stream and exposes all write operations for it.
 */
interface LinkGroupsRepository {

    val groups: Flow<List<LinkGroup>>

    suspend fun addGroup(name: String, description: String)

    suspend fun updateGroup(groupId: Long, name: String, description: String)

    suspend fun deleteGroup(groupId: Long)

    suspend fun addLink(groupId: Long, name: String, url: String)

    suspend fun updateLink(groupId: Long, linkId: Long, name: String, url: String)

    suspend fun deleteLink(groupId: Long, linkId: Long)
}
