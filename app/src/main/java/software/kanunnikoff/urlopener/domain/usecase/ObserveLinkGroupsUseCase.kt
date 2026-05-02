package software.kanunnikoff.urlopener.domain.usecase

import kotlinx.coroutines.flow.Flow
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

/**
 * Observes saved groups and their links for display.
 */
class ObserveLinkGroupsUseCase(
    private val repository: LinkGroupsRepository,
) {

    operator fun invoke(): Flow<List<LinkGroup>> {
        return repository.groups
    }
}
