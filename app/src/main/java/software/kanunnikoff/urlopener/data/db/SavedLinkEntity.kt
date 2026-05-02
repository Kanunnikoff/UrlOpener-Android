package software.kanunnikoff.urlopener.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Database row for a saved link.
 *
 * The foreign key removes child links automatically when their group is deleted, which keeps
 * deletion logic simple and prevents orphaned records.
 */
@Entity(
    tableName = "saved_links",
    foreignKeys = [
        ForeignKey(
            entity = LinkGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("groupId")],
)
data class SavedLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val groupId: Long,
    val name: String,
    val url: String,
    val createdAt: Long,
    val updatedAt: Long,
)
