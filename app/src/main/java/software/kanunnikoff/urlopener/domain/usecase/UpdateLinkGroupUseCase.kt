package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

/**
 * Updates the name and description of an existing group.
 */
class UpdateLinkGroupUseCase(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(groupId: Long, name: String, description: String) {
        repository.updateGroup(groupId, name, description)
    }
}
