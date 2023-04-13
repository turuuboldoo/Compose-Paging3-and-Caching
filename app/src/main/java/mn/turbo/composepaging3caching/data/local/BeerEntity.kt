package mn.turbo.composepaging3caching.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beers")
data class BeerEntity(
    val name: String,
    val tagline: String,
    var description: String,
    val firstBrewed: String,
    val imageUrl: String?,
    @PrimaryKey var id: Int
)
