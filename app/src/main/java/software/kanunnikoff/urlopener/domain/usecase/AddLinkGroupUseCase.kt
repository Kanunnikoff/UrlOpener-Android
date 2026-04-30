package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

class AddLinkGroupUseCase(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(name: String, description: String) {
        repository.addGroup(name, description)
    }
}
