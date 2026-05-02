package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

/**
 * Adds a new group for saved links.
 */
class AddLinkGroupUseCase @Inject constructor(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(name: String, description: String) {
        repository.addGroup(name, description)
    }
}
