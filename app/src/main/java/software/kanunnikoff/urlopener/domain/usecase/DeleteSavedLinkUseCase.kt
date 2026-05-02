package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

/**
 * Deletes a saved link from a group.
 */
class DeleteSavedLinkUseCase @Inject constructor(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(groupId: Long, linkId: Long) {
        repository.deleteLink(groupId, linkId)
    }
}
