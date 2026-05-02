package software.kanunnikoff.urlopener.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import software.kanunnikoff.urlopener.data.db.GroupWithLinks
import software.kanunnikoff.urlopener.data.db.LinkGroupEntity
import software.kanunnikoff.urlopener.data.db.LinkGroupsDao
import software.kanunnikoff.urlopener.data.db.SavedLinkEntity
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

/**
 * Room-backed implementation of [LinkGroupsRepository].
 *
 * The repository trims user-entered text before storage and maps database records to domain models
 * so presentation code never depends on Room annotations or table shapes.
 */
class AndroidLinkGroupsRepository(
    private val dao: LinkGroupsDao,
) : LinkGroupsRepository {

    override val groups: Flow<List<LinkGroup>> = dao.observeGroups()
        .map { groups ->
            groups.map { it.toDomain() }
        }

    override suspend fun addGroup(name: String, description: String) {
        val now = System.currentTimeMillis()

        dao.insertGroup(
            LinkGroupEntity(
                name = name.trim(),
                description = description.trim(),
                createdAt = now,
                updatedAt = now,
            ),
        )
    }

    override suspend fun updateGroup(groupId: Long, name: String, description: String) {
        dao.updateGroup(
            groupId = groupId,
            name = name.trim(),
            description = description.trim(),
            updatedAt = System.currentTimeMillis(),
        )
    }

    override suspend fun deleteGroup(groupId: Long) {
        dao.deleteGroup(groupId)
    }

    override suspend fun addLink(groupId: Long, name: String, url: String) {
        val now = System.currentTimeMillis()

        dao.insertLink(
            SavedLinkEntity(
                groupId = groupId,
                name = name.trim(),
                url = url.trim(),
                createdAt = now,
                updatedAt = now,
            ),
        )
    }

    override suspend fun updateLink(groupId: Long, linkId: Long, name: String, url: String) {
        dao.updateLink(
            groupId = groupId,
            linkId = linkId,
            name = name.trim(),
            url = url.trim(),
            updatedAt = System.currentTimeMillis(),
        )
    }

    override suspend fun deleteLink(groupId: Long, linkId: Long) {
        dao.deleteLink(groupId, linkId)
    }

    override suspend fun replaceGroups(groups: List<LinkGroup>) {
        dao.replaceGroups(groups.map { it.toDatabase() })
    }

    private fun LinkGroup.toDatabase(): GroupWithLinks {
        return GroupWithLinks(
            group = LinkGroupEntity(
                id = id,
                name = name,
                description = description,
                createdAt = createdAt,
                updatedAt = updatedAt,
            ),
            links = links.map { link ->
                SavedLinkEntity(
                    id = link.id,
                    groupId = id,
                    name = link.name,
                    url = link.url,
                    createdAt = link.createdAt,
                    updatedAt = link.updatedAt,
                )
            },
        )
    }

    private fun GroupWithLinks.toDomain(): LinkGroup {
        // Keep the mapping explicit because the database relation model and the UI model have
        // different ownership: Room describes rows, while the domain model describes screen data.
        return LinkGroup(
            id = group.id,
            name = group.name,
            description = group.description,
            createdAt = group.createdAt,
            updatedAt = group.updatedAt,
            links = links.sortedByDescending { it.createdAt }.map {
                SavedLink(
                    id = it.id,
                    name = it.name,
                    url = it.url,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                )
            },
        )
    }
}
