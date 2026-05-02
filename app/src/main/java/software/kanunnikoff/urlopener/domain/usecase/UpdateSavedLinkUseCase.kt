package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

/**
 * Updates a saved link inside a specific group.
 */
class UpdateSavedLinkUseCase @Inject constructor(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(groupId: Long, linkId: Long, name: String, url: String) {
        repository.updateLink(groupId, linkId, name, url)
    }
}
