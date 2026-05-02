package software.kanunnikoff.urlopener.domain.usecase

import javax.inject.Inject
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.service.LinkGroupsJsonCodec

/**
 * Builds portable JSON with all groups and links.
 */
class ExportLinkGroupsJsonUseCase @Inject constructor(
    private val codec: LinkGroupsJsonCodec,
) {

    operator fun invoke(groups: List<LinkGroup>): String {
        return codec.encode(groups)
    }
}
