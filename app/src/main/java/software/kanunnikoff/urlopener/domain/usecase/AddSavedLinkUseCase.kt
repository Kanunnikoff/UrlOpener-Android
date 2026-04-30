package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

class AddSavedLinkUseCase(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(groupId: Long, name: String, url: String) {
        repository.addLink(groupId, name, url)
    }
}
