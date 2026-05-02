package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository
import software.kanunnikoff.urlopener.domain.service.LinkGroupsJsonCodec

/**
 * Reads portable JSON and fully replaces local groups and links.
 */
class ImportLinkGroupsJsonUseCase @Inject constructor(
    private val repository: LinkGroupsRepository,
    private val codec: LinkGroupsJsonCodec,
) {

    suspend operator fun invoke(json: String) {
        repository.replaceGroups(codec.decode(json))
    }
}
