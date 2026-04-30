package software.kanunnikoff.urlopener.domain.usecase

import software.kanunnikoff.urlopener.domain.repository.LinkGroupsRepository

class DeleteLinkGroupUseCase(
    private val repository: LinkGroupsRepository,
) {

    suspend operator fun invoke(groupId: Long) {
        repository.deleteGroup(groupId)
    }
}
