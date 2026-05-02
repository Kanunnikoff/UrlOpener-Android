package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

/**
 * Deletes a group and relies on the persistence layer to remove its child links.
 */
class DeleteLinkGroupUseCase(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(groupId: Long) {
        repository.deleteGroup(groupId)
    }
}
