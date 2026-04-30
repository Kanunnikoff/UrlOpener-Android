package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

class UpdateSavedLinkUseCase(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(groupId: Long, linkId: Long, name: String, url: String) {
        repository.updateLink(groupId, linkId, name, url)
    }
}
