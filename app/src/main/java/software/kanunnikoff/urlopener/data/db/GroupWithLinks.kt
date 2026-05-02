package software.kanunnikoff.urlopener.data.db

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Room projection that loads a group together with all links that belong to it.
 */
data class GroupWithLinks(
    @Embedded
    val group: LinkGroupEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "groupId",
    )
    val links: List<SavedLinkEntity>,
)
