package software.kanunnikoff.urlopener.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link_groups")
data class LinkGroupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String,
)
