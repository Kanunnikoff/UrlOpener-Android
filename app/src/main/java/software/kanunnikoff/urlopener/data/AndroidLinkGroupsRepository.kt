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
        dao.insertGroup(
            LinkGroupEntity(
                name = name.trim(),
                description = description.trim(),
            ),
        )
    }

    override suspend fun updateGroup(groupId: Long, name: String, description: String) {
        dao.updateGroup(
            LinkGroupEntity(
                id = groupId,
                name = name.trim(),
                description = description.trim(),
            ),
        )
    }

    override suspend fun deleteGroup(groupId: Long) {
        dao.deleteGroup(groupId)
    }

    override suspend fun addLink(groupId: Long, name: String, url: String) {
        dao.insertLink(
            SavedLinkEntity(
                groupId = groupId,
                name = name.trim(),
                url = url.trim(),
            ),
        )
    }

    override suspend fun updateLink(groupId: Long, linkId: Long, name: String, url: String) {
        dao.updateLink(
            SavedLinkEntity(
                id = linkId,
                groupId = groupId,
                name = name.trim(),
                url = url.trim(),
            ),
        )
    }

    override suspend fun deleteLink(groupId: Long, linkId: Long) {
        dao.deleteLink(groupId, linkId)
    }

    private fun GroupWithLinks.toDomain(): LinkGroup {
        // Keep the mapping explicit because the database relation model and the UI model have
        // different ownership: Room describes rows, while the domain model describes screen data.
        return LinkGroup(
            id = group.id,
            name = group.name,
            description = group.description,
            links = links.map {
                SavedLink(
                    id = it.id,
                    name = it.name,
                    url = it.url,
                )
            },
        )
    }
}
