package software.kanunnikoff.urlopener.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithLinks(
    @Embedded
    val group: LinkGroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId",
    )
    val links: List<SavedLinkEntity>,
)
