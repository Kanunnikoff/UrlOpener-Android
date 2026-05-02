package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

/**
 * Deletes a saved link from a group.
 */
class DeleteSavedLinkUseCase(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(groupId: Long, linkId: Long) {
        repository.deleteLink(groupId, linkId)
    }
}
